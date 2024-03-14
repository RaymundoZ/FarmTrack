package com.raymundo.farmtrack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raymundo.farmtrack.dto.ReportDto;
import com.raymundo.farmtrack.dto.StatisticsDto;
import com.raymundo.farmtrack.dto.StatisticsItemDto;
import com.raymundo.farmtrack.entity.HarvestRateEntity;
import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.entity.ReportEntity;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.exception.NotFoundException;
import com.raymundo.farmtrack.mapper.ReportMapper;
import com.raymundo.farmtrack.repository.HarvestRateRepository;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.repository.ReportRepository;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Value(value = "${report-service.admin-email}")
    private String adminEmail;

    private final SecurityContextHolderStrategy holderStrategy;
    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HarvestRateRepository harvestRateRepository;
    private final ReportMapper reportMapper;
    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    @Override
    public ReportDto createReport(ReportDto report) {
        ProductEntity product = productRepository.findByName(report.product())
                .orElseThrow(() -> NotFoundException.Code.PRODUCT_NOT_FOUND.get(report.product()));
        Optional<HarvestRateEntity> optional = harvestRateRepository.findByDateAndProduct(LocalDate.now(), product);
        product.setAmount(product.getAmount() + report.amount());
        UserEntity user = (UserEntity) holderStrategy.getContext().getAuthentication().getPrincipal();
        ReportEntity reportEntity = reportMapper.toEntity(report);
        reportEntity.setUser(user);
        reportEntity.setProduct(productRepository.save(product));
        int amount;
        if (optional.isPresent()) {
            HarvestRateEntity harvestRate = optional.get();
            amount = Math.max(harvestRate.getRate() - reportRepository.sumAmountByProductAndCreatedDate(product, LocalDate.now()) - report.amount(), 0);
        } else amount = 0;
        reportEntity.setRateLeft(amount);
        return reportMapper.toDto(reportRepository.save(reportEntity));
    }

    @Override
    public List<StatisticsDto> getGeneralStatistics(StatisticsDto statisticsDto) {
        List<ReportEntity> reports = reportRepository.findAllByCreatedDateBetween(
                statisticsDto.startDate().minusDays(1),
                statisticsDto.endDate().plusDays(1)
        );
        return getSortedMap(reports).entrySet().stream()
                .map(entry1 -> new StatisticsDto(
                        null,
                        null,
                        entry1.getKey().getEmail(),
                        entry1.getValue().entrySet().stream()
                                .map(entry2 -> new StatisticsItemDto(
                                        entry2.getKey().getName(),
                                        entry2.getValue(),
                                        entry2.getKey().getMeasure()
                                )).toList()
                )).toList();
    }

    @Override
    public StatisticsDto getStatisticsByUser(StatisticsDto statisticsDto, String user) {
        UserEntity entity = userRepository.findByEmail(user).orElseThrow(() ->
                NotFoundException.Code.USER_NOT_FOUND.get(user));
        return getGeneralStatistics(statisticsDto).stream()
                .filter(stat -> stat.user().equals(user))
                .findAny().orElse(new StatisticsDto(
                        null,
                        null,
                        entity.getEmail(),
                        List.of()
                ));
    }

    @Override
    @Scheduled(cron = "0 0 20 ? * MON,TUE,WED,THU,FRI")
    public void sendStatisticsEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        StatisticsDto statistics = new StatisticsDto(
                LocalDate.now(),
                LocalDate.now(),
                null,
                null
        );
        message.setTo(adminEmail);
        message.setSubject("Daily farm report");
        try {
            message.setText(objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(getGeneralStatistics(statistics)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    private Map<UserEntity, Map<ProductEntity, Integer>> getSortedMap(List<ReportEntity> reports) {
        return reports.stream()
                .collect(Collectors.groupingBy(
                        ReportEntity::getUser,
                        Collectors.groupingBy(
                                ReportEntity::getProduct,
                                Collectors.summingInt(ReportEntity::getAmount)
                        )
                ));
    }
}

package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.ReportDto;
import com.raymundo.farmtrack.dto.StatisticsDto;
import com.raymundo.farmtrack.dto.StatisticsItemDto;
import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.entity.ReportEntity;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.exception.NotFoundException;
import com.raymundo.farmtrack.mapper.ReportMapper;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.repository.ReportRepository;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SecurityContextHolderStrategy holderStrategy;
    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    @Override
    public ReportDto createReport(ReportDto report) {
        ProductEntity product = productRepository.findByName(report.product())
                .orElseThrow(() -> NotFoundException.Code.PRODUCT_NOT_FOUND.get(report.product()));
        product.setAmount(product.getAmount() + report.amount());
        UserEntity user = (UserEntity) holderStrategy.getContext().getAuthentication().getPrincipal();
        ReportEntity reportEntity = reportMapper.toEntity(report);
        reportEntity.setUser(user);
        reportEntity.setProduct(productRepository.save(product));
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

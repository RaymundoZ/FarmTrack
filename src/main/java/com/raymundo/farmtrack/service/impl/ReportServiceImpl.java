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
import com.raymundo.farmtrack.mapper.ReportMapper;
import com.raymundo.farmtrack.repository.HarvestRateRepository;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.repository.ReportRepository;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.ReportService;
import com.raymundo.farmtrack.util.exception.NotFoundException;
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

/**
 * Implementation of the {@link ReportService} interface for managing reports.
 * <p>
 * This service provides methods for creating reports, retrieving general and user-specific
 * statistics, and sending daily farm statistics emails to the admin. It interacts with
 * the report repository, product repository, user repository, and harvest rate repository
 * to perform CRUD operations on report entities and retrieve relevant data for statistics.
 *
 * @author RaymundoZ
 */
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

    /**
     * Creates a new report.
     * <p>
     * This method retrieves the product entity associated with the provided product name
     * from the product repository. If the product is not found, a {@link NotFoundException}
     * is thrown indicating that the product was not found. It then checks if a harvest rate
     * entry for the current date and the specified product exists in the harvest rate repository.
     * If an entry exists, it calculates the remaining rate left for reporting. The method also
     * updates the product's amount based on the reported amount. The report entity is then created
     * based on the provided report information, and it's associated with the user who created the report
     * and the product involved. The report entity is saved using the report repository, and the
     * resulting report information is converted to a {@link ReportDto} object and returned.
     *
     * @param report A {@link ReportDto} object containing the report information.
     * @return A {@link ReportDto} object representing the created report.
     * @throws NotFoundException Thrown when the product with the specified name is not found.
     */
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

    /**
     * Retrieves general statistics based on the provided date range.
     * <p>
     * This method retrieves all report entities within the specified date range from the
     * report repository. It then calculates and aggregates statistics based on the reports,
     * grouping them by user email, product name, and measure. The resulting statistics are
     * encapsulated in {@link StatisticsDto} objects containing user email and a list of
     * {@link StatisticsItemDto} objects representing product statistics. The list is sorted
     * based on the number of reported items per user email. Finally, the list of statistics
     * DTOs is returned.
     *
     * @param statisticsDto A {@link StatisticsDto} object containing the start and end dates
     *                      for the statistics retrieval.
     * @return A list of {@link StatisticsDto} objects representing general statistics.
     */
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

    /**
     * Retrieves statistics for a specific user based on the provided date range.
     * <p>
     * This method retrieves general statistics for the specified date range using the
     * {@link #getGeneralStatistics(StatisticsDto)} method. It then filters the statistics
     * by the provided user email and returns the statistics for that user encapsulated in
     * a {@link StatisticsDto} object. If no statistics are found for the user within the
     * specified date range, a default empty statistics object with the user's email is returned.
     *
     * @param statisticsDto A {@link StatisticsDto} object containing the start and end dates
     *                      for the statistics retrieval.
     * @param user          The email address of the user for whom statistics are to be retrieved.
     * @return A {@link StatisticsDto} object representing statistics for the specified user.
     * @throws NotFoundException Thrown when the user with the specified email address is not found.
     */
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

    /**
     * Sends daily farm statistics email.
     * <p>
     * This method is scheduled to run every weekday (Monday to Friday) at 8:00 PM
     * (scheduled using a cron expression). It constructs a {@link SimpleMailMessage}
     * containing the daily farm report statistics retrieved using {@link #getGeneralStatistics(StatisticsDto)}.
     * The report is sent to the admin email address specified in the application properties.
     *
     * @throws RuntimeException Thrown if an error occurs during JSON serialization of the statistics.
     */
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

    /**
     * Creates a sorted map of user-product statistics.
     * <p>
     * This method takes a list of report entities and aggregates the statistics by
     * grouping them first by user and then by product. The result is a map where each
     * user entity is associated with a map of product entities and their corresponding
     * reported amounts. The map is sorted based on the number of reported items per user.
     *
     * @param reports A list of {@link ReportEntity} objects containing the reported statistics.
     * @return A sorted map of user-product statistics.
     */
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

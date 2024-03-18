package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.ReportDto;
import com.raymundo.farmtrack.dto.StatisticsDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles report-related operations.
 * <p>
 * This class provides REST endpoints for creating reports and retrieving statistics.
 * It utilizes the {@link ReportService} to perform these operations.
 *
 * @author RaymundoZ
 */
@RestController
@RequestMapping(value = "/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Endpoint for creating a report.
     * <p>
     * This method handles the creation of a report by accepting a POST request with a JSON body
     * containing the report information in the form of {@link ReportDto}. The report information
     * is validated using the {@link Valid} annotation. If the report is successfully created
     * by the {@link ReportService}, the method returns a {@link ResponseEntity} with a success message
     * and the created report information in {@link ReportDto} format, along with an HTTP status code
     * 201 (CREATED).
     *
     * @param reportDto A {@link ReportDto} object containing the report information.
     * @return A {@link ResponseEntity} containing a success message and the created report information upon successful creation.
     */
    @PostMapping
    public ResponseEntity<SuccessDto<ReportDto>> createReport(@Valid @RequestBody ReportDto reportDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Report successfully created",
                        reportService.createReport(reportDto)
                ));
    }

    /**
     * Endpoint for retrieving general statistics.
     * <p>
     * This method handles the retrieval of general statistics by accepting a GET request.
     * It retrieves general statistics using the provided {@link StatisticsDto} object as criteria
     * for filtering data. The criteria object is validated using the {@link Valid} annotation.
     * The method returns a {@link ResponseEntity} with a success message and a list of general
     * statistics in {@link StatisticsDto} format, along with an HTTP status code 200 (OK).
     *
     * @param statisticsDto A {@link StatisticsDto} object containing criteria for retrieving general statistics.
     * @return A {@link ResponseEntity} containing a success message and a list of general statistics upon successful retrieval.
     */
    @GetMapping(value = "/stat")
    public ResponseEntity<SuccessDto<List<StatisticsDto>>> getGeneralStatistics(@Valid @RequestBody StatisticsDto statisticsDto) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "General statistics successfully received",
                reportService.getGeneralStatistics(statisticsDto)
        ));
    }

    /**
     * Endpoint for retrieving general statistics by user.
     * <p>
     * This method handles the retrieval of general statistics for a specific user by accepting a GET request
     * with a path variable specifying the user's email address. It also accepts a {@link StatisticsDto} object
     * containing criteria for filtering data, which is validated using the {@link Valid} annotation.
     * The method retrieves general statistics for the specified user using the provided criteria
     * from the {@link ReportService} and returns a {@link ResponseEntity} with a success message and
     * the general statistics for the user in {@link StatisticsDto} format, along with an HTTP status code 200 (OK).
     *
     * @param statisticsDto A {@link StatisticsDto} object containing criteria for retrieving general statistics.
     * @param userEmail     The email address of the user for whom general statistics are to be retrieved.
     * @return A {@link ResponseEntity} containing a success message and the general statistics for the user upon successful retrieval.
     */
    @GetMapping(value = "/stat/{userEmail}")
    public ResponseEntity<SuccessDto<StatisticsDto>> getGeneralStatistics(@Valid @RequestBody StatisticsDto statisticsDto,
                                                                          @PathVariable String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "User statistics successfully received",
                reportService.getStatisticsByUser(statisticsDto, userEmail)
        ));
    }
}

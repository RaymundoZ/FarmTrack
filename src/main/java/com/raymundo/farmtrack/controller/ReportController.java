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

@RestController
@RequestMapping(value = "/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<SuccessDto<ReportDto>> createReport(@Valid @RequestBody ReportDto reportDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Report successfully created",
                        reportService.createReport(reportDto)
                ));
    }

    @GetMapping(value = "/stat")
    public ResponseEntity<SuccessDto<List<StatisticsDto>>> getGeneralStatistics(@Valid @RequestBody StatisticsDto statisticsDto) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "General statistics successfully received",
                reportService.getGeneralStatistics(statisticsDto)
        ));
    }

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

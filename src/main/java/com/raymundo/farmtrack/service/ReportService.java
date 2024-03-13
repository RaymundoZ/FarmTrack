package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.ReportDto;
import com.raymundo.farmtrack.dto.StatisticsDto;

import java.util.List;

public interface ReportService {

    ReportDto createReport(ReportDto report);

    List<StatisticsDto> getGeneralStatistics(StatisticsDto statisticsDto);

    StatisticsDto getStatisticsByUser(StatisticsDto statisticsDto, String user);

    void sendStatisticsEmail();
}

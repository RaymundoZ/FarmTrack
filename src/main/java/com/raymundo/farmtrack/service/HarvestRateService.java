package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.HarvestRateDto;

import java.time.LocalDate;
import java.util.List;

public interface HarvestRateService {

    HarvestRateDto createHarvestRate(HarvestRateDto harvestRate);

    List<HarvestRateDto> getAllHarvestRates();

    List<HarvestRateDto> getHarvestRatesByDate(LocalDate date);

    List<HarvestRateDto> getHarvestRatesByProduct(String product);
}

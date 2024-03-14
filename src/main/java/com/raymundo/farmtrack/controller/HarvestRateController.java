package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.HarvestRateDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.HarvestRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/harvest")
@RequiredArgsConstructor
public class HarvestRateController {

    private final HarvestRateService harvestRateService;

    @PostMapping
    public ResponseEntity<SuccessDto<HarvestRateDto>> createHarvestRate(@Valid @RequestBody HarvestRateDto harvestRateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Harvest rate successfully created",
                        harvestRateService.createHarvestRate(harvestRateDto)
                ));
    }

    @GetMapping
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getAllHarvestRates() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All harvest rates successfully received",
                harvestRateService.getAllHarvestRates()
        ));
    }

    @GetMapping(value = "/date/{date}")
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getHarvestRatesByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Harvest rates by date successfully received",
                harvestRateService.getHarvestRatesByDate(date)
        ));
    }

    @GetMapping(value = "/product/{product}")
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getHarvestRatesByProduct(@PathVariable String product) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Harvest rates by product successfully received",
                harvestRateService.getHarvestRatesByProduct(product)
        ));
    }
}

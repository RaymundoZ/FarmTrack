package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.HarvestRateDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.HarvestRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class that handles harvest rate-related operations.
 * <p>
 * This class provides REST endpoints for creating, retrieving, and managing harvest rates.
 * It utilizes the {@link HarvestRateService} to perform these operations.
 *
 * @author RaymundoZ
 */
@Tag(name = "HarvestRateController", description = "Controller class that handles harvest rate-related operations")
@RestController
@RequestMapping(value = "/harvest")
@RequiredArgsConstructor
public class HarvestRateController {

    private final HarvestRateService harvestRateService;

    /**
     * Endpoint for creating a harvest rate.
     * <p>
     * This method handles the creation of a harvest rate by accepting a POST request with a JSON body
     * containing the harvest rate information in the form of {@link HarvestRateDto}. The harvest rate information
     * is validated using the {@link Valid} annotation. If the harvest rate is successfully created
     * by the {@link HarvestRateService}, the method returns a {@link ResponseEntity} with a success message
     * and the created harvest rate information in {@link HarvestRateDto} format, along with an HTTP status code
     * 201 (CREATED).
     *
     * @param harvestRateDto A {@link HarvestRateDto} object containing the harvest rate information.
     * @return A {@link ResponseEntity} containing a success message and the created harvest rate information upon successful creation.
     */
    @Operation(summary = "Endpoint for creating a harvest rate")
    @PostMapping
    public ResponseEntity<SuccessDto<HarvestRateDto>> createHarvestRate(@Valid @RequestBody HarvestRateDto harvestRateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Harvest rate successfully created",
                        harvestRateService.createHarvestRate(harvestRateDto)
                ));
    }

    /**
     * Endpoint for retrieving all harvest rates.
     * <p>
     * This method handles the retrieval of all harvest rates by accepting a GET request.
     * It retrieves all harvest rates using the {@link HarvestRateService} and returns a {@link ResponseEntity}
     * with a success message and a list of all harvest rates in {@link HarvestRateDto} format, along with
     * an HTTP status code 200 (OK).
     *
     * @return A {@link ResponseEntity} containing a success message and a list of all harvest rates upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving all harvest rates")
    @GetMapping
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getAllHarvestRates() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All harvest rates successfully received",
                harvestRateService.getAllHarvestRates()
        ));
    }

    /**
     * Endpoint for retrieving harvest rates by date.
     * <p>
     * This method handles the retrieval of harvest rates for a specific date by accepting a GET request
     * with a path variable specifying the date. It retrieves harvest rates for the specified date using
     * the {@link HarvestRateService} and returns a {@link ResponseEntity} with a success message and a list
     * of harvest rates for the date in {@link HarvestRateDto} format, along with an HTTP status code 200 (OK).
     *
     * @param date The date for which harvest rates are to be retrieved.
     * @return A {@link ResponseEntity} containing a success message and a list of harvest rates for the date upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving harvest rates by date")
    @GetMapping(value = "/date/{date}")
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getHarvestRatesByDate(@PathVariable
                                                                                  @Parameter(description = "The date for which harvest rates are to be retrieved")
                                                                                  LocalDate date) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Harvest rates by date successfully received",
                harvestRateService.getHarvestRatesByDate(date)
        ));
    }

    /**
     * Endpoint for retrieving harvest rates by product.
     * <p>
     * This method handles the retrieval of harvest rates for a specific product by accepting a GET request
     * with a path variable specifying the product name. It retrieves harvest rates for the specified product
     * using the {@link HarvestRateService} and returns a {@link ResponseEntity} with a success message and a list
     * of harvest rates for the product in {@link HarvestRateDto} format, along with an HTTP status code 200 (OK).
     *
     * @param product The name of the product for which harvest rates are to be retrieved.
     * @return A {@link ResponseEntity} containing a success message and a list of harvest rates for the product upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving harvest rates by product")
    @GetMapping(value = "/product/{product}")
    public ResponseEntity<SuccessDto<List<HarvestRateDto>>> getHarvestRatesByProduct(@PathVariable
                                                                                     @Parameter(description = "The name of the product for which harvest rates are to be retrieved")
                                                                                     String product) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Harvest rates by product successfully received",
                harvestRateService.getHarvestRatesByProduct(product)
        ));
    }
}

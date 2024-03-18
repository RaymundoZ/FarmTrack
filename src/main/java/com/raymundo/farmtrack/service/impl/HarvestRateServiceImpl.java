package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.HarvestRateDto;
import com.raymundo.farmtrack.entity.HarvestRateEntity;
import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.mapper.HarvestRateMapper;
import com.raymundo.farmtrack.repository.HarvestRateRepository;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.service.HarvestRateService;
import com.raymundo.farmtrack.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link HarvestRateService} interface for managing harvest rate entries.
 * <p>
 * This service handles operations related to creating, updating, and retrieving harvest rate entries.
 * It utilizes a {@link HarvestRateRepository} for accessing harvest rate data, a {@link ProductRepository}
 * for accessing product data, and a {@link HarvestRateMapper} for mapping harvest rate entities to DTOs.
 *
 * @author RaymundoZ
 */
@Service
@RequiredArgsConstructor
public class HarvestRateServiceImpl implements HarvestRateService {

    private final HarvestRateRepository harvestRateRepository;
    private final ProductRepository productRepository;
    private final HarvestRateMapper harvestRateMapper;

    /**
     * Creates or updates a harvest rate entry for a product.
     * <p>
     * This method retrieves the product entity associated with the provided product name
     * from the product repository. If the product is not found, a {@link NotFoundException}
     * is thrown indicating that the product was not found. It then checks if a harvest rate
     * entry for the specified date and product exists in the harvest rate repository. If
     * an entry exists, it updates the existing entry with the provided rate value; otherwise,
     * it creates a new harvest rate entity based on the provided harvest rate information.
     * The harvest rate entity is then saved using the harvest rate repository, and the
     * updated or newly created harvest rate information is converted to a
     * {@link HarvestRateDto} object using {@link HarvestRateMapper} and returned.
     *
     * @param harvestRate A {@link HarvestRateDto} object containing the harvest rate information.
     * @return A {@link HarvestRateDto} object representing the created or updated harvest rate entry.
     * @throws NotFoundException Thrown when the product with the specified name is not found.
     */
    @Override
    public HarvestRateDto createHarvestRate(HarvestRateDto harvestRate) {
        ProductEntity product = productRepository.findByName(harvestRate.product())
                .orElseThrow(() -> NotFoundException.Code.PRODUCT_NOT_FOUND.get(harvestRate.product()));
        Optional<HarvestRateEntity> optional = harvestRateRepository.findByDateAndProduct(harvestRate.date(), product);
        HarvestRateEntity entity;
        if (optional.isPresent()) {
            entity = optional.get();
            entity.setRate(harvestRate.rate());
        } else {
            entity = harvestRateMapper.toEntity(harvestRate);
            entity.setProduct(product);
        }
        return harvestRateMapper.toDto(harvestRateRepository.save(entity));
    }

    /**
     * Retrieves a list of all harvest rate entries.
     * <p>
     * This method retrieves all harvest rate entities from the harvest rate repository
     * and maps them to {@link HarvestRateDto} objects using {@link HarvestRateMapper}.
     * The resulting list of harvest rate DTOs is returned.
     *
     * @return A list of {@link HarvestRateDto} objects representing all harvest rate entries.
     */
    @Override
    public List<HarvestRateDto> getAllHarvestRates() {
        return harvestRateRepository.findAll().stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of harvest rate entries for a specific date.
     * <p>
     * This method retrieves all harvest rate entities associated with the provided date
     * from the harvest rate repository and maps them to {@link HarvestRateDto} objects
     * using {@link HarvestRateMapper}. The resulting list of harvest rate DTOs is returned.
     *
     * @param date The date for which harvest rate entries are to be retrieved.
     * @return A list of {@link HarvestRateDto} objects representing harvest rate entries for the specified date.
     */
    @Override
    public List<HarvestRateDto> getHarvestRatesByDate(LocalDate date) {
        return harvestRateRepository.findAllByDate(date).stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of harvest rate entries for a specific product.
     * <p>
     * This method retrieves the product entity associated with the provided product name
     * from the product repository. If the product is not found, a {@link NotFoundException}
     * is thrown indicating that the product was not found. It then retrieves all harvest rate
     * entities associated with the product from the harvest rate repository and maps them to
     * {@link HarvestRateDto} objects using {@link HarvestRateMapper}. The resulting list of
     * harvest rate DTOs is returned.
     *
     * @param product The name of the product for which harvest rate entries are to be retrieved.
     * @return A list of {@link HarvestRateDto} objects representing harvest rate entries for the specified product.
     * @throws NotFoundException Thrown when the product with the specified name is not found.
     */
    @Override
    public List<HarvestRateDto> getHarvestRatesByProduct(String product) {
        ProductEntity entity = productRepository.findByName(product)
                .orElseThrow(() -> NotFoundException.Code.PRODUCT_NOT_FOUND.get(product));
        return harvestRateRepository.findAllByProduct(entity).stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }
}

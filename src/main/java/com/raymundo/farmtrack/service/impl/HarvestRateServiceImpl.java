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

@Service
@RequiredArgsConstructor
public class HarvestRateServiceImpl implements HarvestRateService {

    private final HarvestRateRepository harvestRateRepository;
    private final ProductRepository productRepository;
    private final HarvestRateMapper harvestRateMapper;

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

    @Override
    public List<HarvestRateDto> getAllHarvestRates() {
        return harvestRateRepository.findAll().stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }

    @Override
    public List<HarvestRateDto> getHarvestRatesByDate(LocalDate date) {
        return harvestRateRepository.findAllByDate(date).stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }

    @Override
    public List<HarvestRateDto> getHarvestRatesByProduct(String product) {
        ProductEntity entity = productRepository.findByName(product)
                .orElseThrow(() -> NotFoundException.Code.PRODUCT_NOT_FOUND.get(product));
        return harvestRateRepository.findAllByProduct(entity).stream()
                .map(harvestRateMapper::toDto)
                .toList();
    }
}

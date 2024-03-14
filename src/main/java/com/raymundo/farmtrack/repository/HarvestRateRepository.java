package com.raymundo.farmtrack.repository;

import com.raymundo.farmtrack.entity.HarvestRateEntity;
import com.raymundo.farmtrack.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HarvestRateRepository extends JpaRepository<HarvestRateEntity, UUID> {

    Optional<HarvestRateEntity> findByDateAndProduct(LocalDate date, ProductEntity product);

    List<HarvestRateEntity> findAllByDate(LocalDate date);

    List<HarvestRateEntity> findAllByProduct(ProductEntity product);
}

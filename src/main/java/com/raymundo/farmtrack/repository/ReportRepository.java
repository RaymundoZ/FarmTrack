package com.raymundo.farmtrack.repository;

import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

    List<ReportEntity> findAllByCreatedDateBetween(LocalDate start, LocalDate end);

    @Query(value = "select coalesce(sum(r.amount), 0) from ReportEntity r where r.product = ?1 and r.createdDate = ?2")
    Integer sumAmountByProductAndCreatedDate(ProductEntity product, LocalDate date);
}

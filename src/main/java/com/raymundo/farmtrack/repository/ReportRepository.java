package com.raymundo.farmtrack.repository;

import com.raymundo.farmtrack.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

    List<ReportEntity> findAllByCreatedDateBetween(LocalDate start, LocalDate end);
}

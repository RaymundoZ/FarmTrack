package com.raymundo.farmtrack.repository;

import com.raymundo.farmtrack.entity.GradeEntity;
import com.raymundo.farmtrack.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, UUID> {

    Optional<GradeEntity> findByCreatedDateAndUser(LocalDate localDate, UserEntity user);

    List<GradeEntity> findAllByUser(UserEntity user);
}

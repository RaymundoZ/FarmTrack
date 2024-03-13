package com.raymundo.farmtrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "_grade")
@Getter
@Setter
public class GradeEntity extends BaseEntity {

    private Integer grade;

    @ManyToOne
    private UserEntity user;
}

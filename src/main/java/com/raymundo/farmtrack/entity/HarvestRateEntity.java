package com.raymundo.farmtrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "_harvest_rate")
@Getter
@Setter
public class HarvestRateEntity extends BaseEntity {

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private ProductEntity product;
}

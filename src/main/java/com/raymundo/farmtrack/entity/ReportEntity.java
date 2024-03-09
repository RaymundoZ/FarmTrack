package com.raymundo.farmtrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "_report")
@Getter
@Setter
public class ReportEntity extends BaseEntity {

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    private UserEntity user;
}

package com.raymundo.farmtrack.entity;

import jakarta.persistence.*;
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

    @Transient
    private Integer rateLeft;
}

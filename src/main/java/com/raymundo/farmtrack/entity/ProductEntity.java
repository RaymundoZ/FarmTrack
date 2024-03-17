package com.raymundo.farmtrack.entity;

import com.raymundo.farmtrack.util.enumeration.Measure;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "_product")
@Getter
@Setter
public class ProductEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "measure")
    private Measure measure;
}

package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "restaurant")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant implements Serializable {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "balance")
    private BigDecimal balance;
}

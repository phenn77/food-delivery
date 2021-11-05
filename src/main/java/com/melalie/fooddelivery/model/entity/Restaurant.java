package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;

@Entity
@Table(name = "restaurant")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "menu")
    private Clob menu;
}

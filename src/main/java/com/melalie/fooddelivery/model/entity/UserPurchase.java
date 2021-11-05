package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_purchases")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPurchase implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @Column(name = "dish")
    private String dish;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date")
    private Timestamp date;
}

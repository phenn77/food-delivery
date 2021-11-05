package com.melalie.fooddelivery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_purchase")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPurchase implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "dish")
    private String dish;

    @Column(name = "restaurantName")
    private String restaurantName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "date")
    private String date;

    @Column(name = "user")
    private String user;
}

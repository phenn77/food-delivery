package com.melalie.fooddelivery.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPurchase implements Serializable {

    private String dish;

    @JsonProperty(value = "restaurant_name")
    private String restaurantName;

    private double amount;
    private String date;
}

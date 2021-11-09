package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularRestaurantResponse implements Serializable {

    private List<Restaurant> restaurantList;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Restaurant implements Serializable {
        private String name;
        private Integer numberOfTransactions;
        private BigDecimal totalAmount;
    }
}

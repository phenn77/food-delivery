package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByRestaurantResponse implements Serializable {

    private List<Restaurants> restaurantsList;


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Restaurants implements Serializable {
        private String id;
        private String restaurantName;
        private Map<String, Integer> transactionList;
    }
}

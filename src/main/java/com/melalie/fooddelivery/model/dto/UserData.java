package com.melalie.fooddelivery.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {

    private String name;
    private String location;
    private String balance;
    private List<Purchase> purchases;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Purchase implements Serializable {
        private String dish;

        @JsonProperty(value = "restaurant_name")
        private String restaurantName;
        private String amount;
        private String date;
        private String user;
    }
}

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
public class RestaurantData implements Serializable {

    private String name;
    private String location;
    private String balance;

    @JsonProperty(value = "business_hours")
    private String businessHours;

    private List<MenuData> menu;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuData implements Serializable {
        private String name;
        private String price;
    }
}

package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantsResponse implements Serializable {

    private String dishName;
    private List<String> restaurantList;
}

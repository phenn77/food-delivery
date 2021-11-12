package com.melalie.fooddelivery.model.response;

import com.melalie.fooddelivery.model.dto.RestaurantWithDish;
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
public class RestaurantDishesResponse implements Serializable {

    private List<RestaurantWithDish> restaurantList;
}

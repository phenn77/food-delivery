package com.melalie.fooddelivery.model.dto;

import com.melalie.fooddelivery.model.entity.Menu;
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
public class RestaurantWithDish implements Serializable {

    private String restaurantName;
    private Integer menuQuantity;
    private List<Menu> menuList;
}

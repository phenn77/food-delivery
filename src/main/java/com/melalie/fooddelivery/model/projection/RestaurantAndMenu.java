package com.melalie.fooddelivery.model.projection;

import java.math.BigDecimal;

public interface RestaurantAndMenu {

    String getRestaurantId();

    String getRestaurantName();

    String getDishName();

    BigDecimal getPrice();
}

package com.melalie.fooddelivery.model.projection;

public interface RestaurantTransactionData {

    String getRestaurantId();

    String getRestaurantName();

    String getDish();

    double getAmount();
}

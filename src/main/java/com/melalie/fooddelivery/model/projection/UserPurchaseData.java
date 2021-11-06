package com.melalie.fooddelivery.model.projection;

import java.sql.Timestamp;

public interface UserPurchaseData {

    String getDish();

    String getRestaurantName();

    double getAmount();

    Timestamp getDate();

    String getName();
}

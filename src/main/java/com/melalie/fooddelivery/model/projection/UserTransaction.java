package com.melalie.fooddelivery.model.projection;

import java.math.BigDecimal;

public interface UserTransaction {

    String getName();

    Integer getTotalTransaction();

    BigDecimal getTotalAmount();
}

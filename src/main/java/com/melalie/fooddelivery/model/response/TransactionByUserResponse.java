package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByUserResponse implements Serializable {

    private String name;
    private BigDecimal totalSpent;
    private List<Purchase> userPurchases;
}

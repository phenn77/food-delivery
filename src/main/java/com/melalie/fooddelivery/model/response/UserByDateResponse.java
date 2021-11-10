package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserByDateResponse implements Serializable {
    private Map<String, Integer> totalUsers;
    private BigDecimal transactionAmount;

}

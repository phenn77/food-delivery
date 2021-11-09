package com.melalie.fooddelivery.model.response;

import com.melalie.fooddelivery.model.dto.Purchase;
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

    private List<UserTrx> users;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTrx implements Serializable {
        private String id;
        private String name;
        private BigDecimal totalSpent;
        private List<Purchase> userPurchases;
    }
}

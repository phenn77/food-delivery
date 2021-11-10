package com.melalie.fooddelivery.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUsersResponse implements Serializable {

    private List<UserTrx> users;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserTrx implements Serializable {
        private String name;
        private Integer totalTransaction;
        private BigDecimal totalAmount;
    }
}

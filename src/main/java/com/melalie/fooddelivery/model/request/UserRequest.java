package com.melalie.fooddelivery.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {

    private BigDecimal transactionAmount;

    @NotNull
    private String fromDate;

    @NotNull
    private String toDate;
}

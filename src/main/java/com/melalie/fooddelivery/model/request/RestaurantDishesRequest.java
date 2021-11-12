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
public class RestaurantDishesRequest implements Serializable {

    @NotNull
    private Integer totalDishFrom;

    @NotNull
    private Integer totalDishTo;

    @NotNull
    private BigDecimal priceFrom;

    @NotNull
    private BigDecimal priceTo;
}

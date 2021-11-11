package com.melalie.fooddelivery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuPurchase implements Serializable {

    private String menuId;

    private String name;

    private Integer quantity;

    private BigDecimal amount;
}

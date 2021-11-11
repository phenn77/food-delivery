package com.melalie.fooddelivery.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuPurchaseRequest implements Serializable {

    @NotBlank
    private String menuId;

    @NotNull
    private Integer quantity;
}

package com.melalie.fooddelivery.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseRequest implements Serializable {

    @NotBlank
    private String userId;

    @NotBlank
    private String restaurantId;

    @NotNull
    private List<MenuPurchaseRequest> purchases;
}

package com.melalie.fooddelivery.model.response;

import com.melalie.fooddelivery.model.dto.MenuPurchase;
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
public class CreatePurchaseResponse implements Serializable {

    private String status;

    private String restaurantName;

    private BigDecimal previousBalance;

    private BigDecimal currentBalance;

    private String user;

    private List<MenuPurchase> purchases;
}

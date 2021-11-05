package com.melalie.fooddelivery.model.response;

import com.melalie.fooddelivery.model.entity.UserPurchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByUserResponse implements Serializable {

    private String name;
    private List<UserPurchase> userPurchases;
}

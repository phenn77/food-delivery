package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import com.melalie.fooddelivery.model.response.Purchase;
import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetTransactionsByUserService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetTransactionsByUserService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public TransactionByUserResponse execute(String userId) {
        List<UserPurchaseData> userData = userPurchaseRepository.findByUserId(userId);

        if (userData.isEmpty()) {
            return null;
        }

        double userSpent = userData
                .stream()
                .mapToDouble(UserPurchaseData::getAmount)
                .sum();

        String username = userData
                .stream()
                .map(UserPurchaseData::getName)
                .findFirst()
                .orElse(StringUtils.EMPTY);

        return TransactionByUserResponse.builder()
                .name(username)
                .totalSpent(BigDecimal.valueOf(userSpent))
                .userPurchases(retrieveUserPurchases(userData))
                .build();
    }

    private List<Purchase> retrieveUserPurchases(List<UserPurchaseData> userPurchasesData) {
        return userPurchasesData
                .stream()
                .map(data -> Purchase.builder()
                        .restaurantName(data.getRestaurantName())
                        .dish(data.getDish())
                        .amount(data.getAmount())
                        .date(data.getDate())
                        .build())
                .sorted(Comparator.comparing(Purchase::getDate).reversed())
                .collect(Collectors.toList());
    }
}

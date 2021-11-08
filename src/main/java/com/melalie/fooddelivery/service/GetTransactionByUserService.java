package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import com.melalie.fooddelivery.model.dto.Purchase;
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
public class GetTransactionByUserService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetTransactionByUserService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public TransactionByUserResponse execute(String userId, String name) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(name)) {
            log.error("Request is empty. User ID : {}, Name: {}", userId, name);
            return null;
        }

        List<UserPurchaseData> userData;
        if (StringUtils.isNotBlank(userId)) {
            userData = userPurchaseRepository.findByUserId(userId);
        } else {
            userData = userPurchaseRepository.findByUserName(name);
        }

        if (userData.isEmpty()) {
            log.error("User not found.");
            return null;
        }

        double userSpent = userData
                .stream()
                .mapToDouble(UserPurchaseData::getAmount)
                .sum();

        return TransactionByUserResponse.builder()
                .id(userData.get(0).getUserId())
                .name(userData.get(0).getName())
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

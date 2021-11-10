package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import com.melalie.fooddelivery.model.dto.Purchase;
import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetTransactionByUserService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetTransactionByUserService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public TransactionByUserResponse execute(String userId, String name) throws Exception {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(name)) {
            log.error("Request is empty. User ID : {}, Name: {}", userId, name);
            throw new Exception("Payload not complete.");
        }

        List<UserPurchaseData> userData;
        if (StringUtils.isNotBlank(userId)) {
            userData = userPurchaseRepository.findByUserId(userId);
        } else {
            String nameParam = "%" + name + "%";
            userData = userPurchaseRepository.findByUserName(nameParam);
        }

        if (userData.isEmpty()) {
            log.error("User not found.");
            throw new Exception("User not found.");
        }


        return TransactionByUserResponse.builder()
                .users(retrieveUserPurchases(userData))
                .build();
    }

    private List<TransactionByUserResponse.UserTrx> retrieveUserPurchases(List<UserPurchaseData> userPurchasesData) {
        Map<String, List<Purchase>> result = new HashMap<>();

        var userNames = userPurchasesData
                .stream()
                .collect(Collectors.toMap(UserPurchaseData::getUserId, UserPurchaseData::getName, (oldValue, newValue) -> newValue));

        userPurchasesData
                .forEach(data -> {
                    var purchase = Purchase.builder()
                            .restaurantName(data.getRestaurantName())
                            .dish(data.getDish())
                            .amount(data.getAmount())
                            .date(data.getDate())
                            .build();

                    if (!result.containsKey(data.getUserId())) {
                        result.put(data.getUserId(), new ArrayList<>());
                    }

                    result.get(data.getUserId()).add(purchase);
                });

        return result
                .entrySet()
                .stream()
                .map(data -> {
                    var userSpent = data.getValue()
                            .stream()
                            .mapToDouble(Purchase::getAmount)
                            .sum();

                    return TransactionByUserResponse.UserTrx.builder()
                            .id(data.getKey())
                            .name(userNames.get(data.getKey()))
                            .totalSpent(new BigDecimal(userSpent).setScale(2, RoundingMode.DOWN))
                            .userPurchases(
                                    data.getValue()
                                            .stream()
                                            .sorted(Comparator.comparing(Purchase::getDate).reversed())
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .sorted(Comparator.comparing(TransactionByUserResponse.UserTrx::getName))
                .collect(Collectors.toList());
    }
}

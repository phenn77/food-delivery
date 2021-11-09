package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.PopularRestaurantRequest;
import com.melalie.fooddelivery.model.response.PopularRestaurantResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetPopularRestaurantService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetPopularRestaurantService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public PopularRestaurantResponse execute(PopularRestaurantRequest request) {
        if (Objects.isNull(request.getByTransaction()) && Objects.isNull(request.getByAmount())) {
            return null;
        }

        return PopularRestaurantResponse.builder()
                .restaurantList(retrieveData(request))
                .build();
    }

    private List<PopularRestaurantResponse.Restaurant> retrieveData(PopularRestaurantRequest request) {
        Map<String, Integer> byTransaction = new HashMap<>();
        Map<String, BigDecimal> byAmount = new HashMap<>();
        Map<String, String> name = new HashMap<>();

        userPurchaseRepository.retrieveTransactions()
                .forEach(trx -> {
                    if (!name.containsKey(trx.getRestaurantId())) {
                        name.put(trx.getRestaurantId(), trx.getRestaurantName());
                    }

                    if (byTransaction.containsKey(trx.getRestaurantId()) && byAmount.containsKey(trx.getRestaurantId())) {
                        int previousTrxCount = byTransaction.get(trx.getRestaurantId());
                        byTransaction.put(trx.getRestaurantId(), previousTrxCount + 1);

                        BigDecimal previousAmt = byAmount.get(trx.getRestaurantId());
                        byAmount.put(trx.getRestaurantId(), previousAmt.add(BigDecimal.valueOf(trx.getAmount())));
                    } else {
                        byTransaction.put(trx.getRestaurantId(), 1);

                        byAmount.put(trx.getRestaurantId(), BigDecimal.valueOf(trx.getAmount()));
                    }
                });

        List<PopularRestaurantResponse.Restaurant> result = byTransaction
                .keySet()
                .stream()
                .map(restId -> PopularRestaurantResponse.Restaurant.builder()
                        .name(name.get(restId))
                        .numberOfTransactions(byTransaction.get(restId))
                        .totalAmount(byAmount.get(restId).setScale(2, RoundingMode.DOWN))
                        .build())
                .collect(Collectors.toList());

        if (BooleanUtils.isTrue(request.getByTransaction())) {
            return result
                    .stream()
                    .sorted(Comparator.comparing(PopularRestaurantResponse.Restaurant::getNumberOfTransactions).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return result
                    .stream()
                    .sorted(Comparator.comparing(PopularRestaurantResponse.Restaurant::getTotalAmount).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }
}

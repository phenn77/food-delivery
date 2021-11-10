package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.Trx;
import com.melalie.fooddelivery.model.projection.RestaurantTransactionData;
import com.melalie.fooddelivery.model.response.TransactionByRestaurantResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetTransactionByRestaurantService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetTransactionByRestaurantService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public TransactionByRestaurantResponse execute(String restaurantId, String restaurantName) throws Exception {
        if (StringUtils.isBlank(restaurantId) && StringUtils.isBlank(restaurantName)) {
            log.error("Request empty. Restaurant ID: {}, Name: {}", restaurantId, restaurantName);

            throw new Exception("Payload not complete.");
        }

        List<TransactionByRestaurantResponse.Restaurants> restaurantTrx = retrieveTransactions(restaurantId, restaurantName);

        return TransactionByRestaurantResponse.builder()
                .restaurantsList(restaurantTrx)
                .build();
    }

    private List<TransactionByRestaurantResponse.Restaurants> retrieveTransactions(String restaurantId, String restaurantName) {
        //<RestaurantId, Trx>
        Map<String, Trx> dataContainer = new HashMap<>();

        List<RestaurantTransactionData> trxData;
        if (StringUtils.isNotBlank(restaurantId)) {
            trxData = userPurchaseRepository.retrieveRestaurantTransactions(restaurantId);
        } else {
            trxData = userPurchaseRepository.retrieveRestaurantTransactionsByName(restaurantName);
        }

        if (trxData.isEmpty()) {
            log.error("Data not found.");
            return new ArrayList<>();
        }

        trxData.forEach(data -> {
            Trx trx = Trx.builder()
                    .restaurantName(data.getRestaurantName())
                    .build();

            if (!dataContainer.containsKey(data.getRestaurantId())) {
                trx.setDishOrdered(new HashMap<>());
                dataContainer.put(data.getRestaurantId(), trx);
            }

            //each restaurants' dishes
            Map<String, Integer> restDishes = dataContainer.get(data.getRestaurantId()).getDishOrdered();

            if (restDishes.containsKey(data.getDish())) {
                Integer value = restDishes.get(data.getDish());

                restDishes.put(data.getDish(), value + 1);
            } else {
                restDishes.put(data.getDish(), 1);
            }
            trx.setDishOrdered(restDishes);

            dataContainer.put(data.getRestaurantId(), trx);
        });

        return dataContainer
                .entrySet()
                .stream()
                .map(trx -> TransactionByRestaurantResponse.Restaurants.builder()
                        .id(trx.getKey())
                        .restaurantName(trx.getValue().getRestaurantName())
                        .transactionList(new TreeMap<>(trx.getValue().getDishOrdered()))
                        .build())
                .sorted(Comparator.comparing(TransactionByRestaurantResponse.Restaurants::getRestaurantName))
                .collect(Collectors.toList());
    }
}

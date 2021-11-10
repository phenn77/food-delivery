package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.model.response.UserByDateResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class GetUsersByDateService {

    private UserPurchaseRepository userPurchaseRepository;

    public GetUsersByDateService(UserPurchaseRepository userPurchaseRepository) {
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public UserByDateResponse execute(UserRequest request) throws Exception {
        if (Objects.isNull(request.getTransactionAmount())) {
            log.error("Amount is mandatory");

            throw new Exception("Payload not complete.");
        }

        return retrieveData(request);
    }

    private UserByDateResponse retrieveData(UserRequest request) throws Exception {
        Map<String, Integer> users = new HashMap<>();

        String fromDate = formatDate(request.getFromDate());
        String toDate = formatDate(request.getToDate());

        Integer usersWithMin = userPurchaseRepository.getMinTransaction(fromDate, toDate, request.getTransactionAmount());
        users.put("Below", usersWithMin);

        Integer usersWithMax = userPurchaseRepository.getMaxTransaction(fromDate, toDate, request.getTransactionAmount());
        users.put("Over", usersWithMax);

        return UserByDateResponse.builder()
                .transactionAmount(request.getTransactionAmount())
                .totalUsers(users)
                .build();
    }

    private String formatDate(String payload) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = sdf.parse(payload);

            return newFormat.format(date);
        } catch (Exception e) {
            log.error("Failed to convert date. Payload: {}", payload);

            throw new Exception("Error parsing date.");
        }
    }
}

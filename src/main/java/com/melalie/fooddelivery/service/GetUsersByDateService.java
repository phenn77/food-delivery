package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.model.response.UserByDateResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.melalie.fooddelivery.util.constant.CommonUtils.formatDate;
import static com.melalie.fooddelivery.util.constant.Constants.YYYYMMDD;
import static com.melalie.fooddelivery.util.constant.Constants.YYYYMMDDHHMMSS;

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

        var fromDate = formatDate(request.getFromDate(), YYYYMMDD, YYYYMMDDHHMMSS);
        var toDate = formatDate(request.getToDate(), YYYYMMDD, YYYYMMDDHHMMSS);

        var usersWithMin = userPurchaseRepository.getMinTransaction(fromDate, toDate, request.getTransactionAmount());
        users.put("Below", usersWithMin);

        var usersWithMax = userPurchaseRepository.getMaxTransaction(fromDate, toDate, request.getTransactionAmount());
        users.put("Over", usersWithMax);

        return UserByDateResponse.builder()
                .transactionAmount(request.getTransactionAmount())
                .totalUsers(users)
                .build();
    }
}

package com.melalie.fooddelivery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melalie.fooddelivery.model.entity.UserPurchase;
import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class GetTransactionsByUserService {

    private UserRepository userRepository;

    public GetTransactionsByUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionByUserResponse execute(String userId) {
        return userRepository.findById(userId)
                .map(data ->
                        TransactionByUserResponse.builder()
                                .name(data.getName())
//                                .userPurchases(retrieveUserPurchases(data.getPurchases()))
                                .build()
                )
                .orElseGet(() -> {
                    log.error("User not found. User ID: {}", userId);

                    return null;
                });
    }
}

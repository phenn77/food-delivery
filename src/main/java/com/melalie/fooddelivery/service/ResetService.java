package com.melalie.fooddelivery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melalie.fooddelivery.model.dto.RestaurantData;
import com.melalie.fooddelivery.model.dto.UserData;
import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.model.entity.User;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import com.melalie.fooddelivery.repository.UserRepository;
import com.melalie.fooddelivery.util.CommonUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ResetService {

    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;

    public ResetService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void execute() {
        log.info("Start resetting and reinsert data");

        //Delete all data existing
        restaurantRepository.deleteAll();
        userRepository.deleteAll();

        retrieveRestaurantData();
        retrieveUserData();
    }

    private void retrieveRestaurantData() {
        List<RestaurantData> restaurantData = new ArrayList<>();

        try {
            restaurantData = objectMapper.readValue(new File("src/main/resources/restaurants.json"), new TypeReference<List<RestaurantData>>() {
            });

            log.info("Restaurant Data: {}", restaurantData.size());
        } catch (Exception e) {
            log.error("Failed to retrieve Restaurants data. Error: {}", (Object) ExceptionUtils.getRootCauseStackTrace(e));
        }

        long currentTime = System.currentTimeMillis();
        List<Restaurant> restaurantList = restaurantData
                .stream()
                .map(data ->
                        Restaurant.builder()
                                .name(data.getName())
                                .location(data.getLocation())
                                .balance(new BigDecimal(data.getBalance()))
                                .businessHours(data.getBusinessHours())
                                .menu(CommonUtils.getClob(data.getMenu().toString()))
                                .build())
                .collect(Collectors.toList());
        restaurantRepository.saveAll(restaurantList);

        log.info("Finish inserting Restaurants data. Time finished: {}", (System.currentTimeMillis() - currentTime));
    }

    private void retrieveUserData() {
        List<UserData> userData = new ArrayList<>();

        try {
            userData = objectMapper.readValue(new File("src/main/resources/users.json"), new TypeReference<List<UserData>>() {
            });

            log.info("User Data: {}", userData.size());
        } catch (Exception e) {
            log.error("Failed to retrieve Restaurants data. Error: {}", (Object) ExceptionUtils.getRootCauseStackTrace(e));
        }

        long currentTime = System.currentTimeMillis();
        List<User> userList = userData
                .stream()
                .map(data ->
                        User.builder()
                                .name(data.getName())
                                .location(data.getLocation())
                                .balance(new BigDecimal(data.getBalance()))
                                .purchases(CommonUtils.getClob(data.getPurchases().toString()))
                                .build())
                .collect(Collectors.toList());

        userRepository.saveAll(userList);

        log.info("Finish inserting Users data. Time finished: {}", (System.currentTimeMillis() - currentTime));
    }
}

package com.melalie.fooddelivery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melalie.fooddelivery.model.dto.RestaurantData;
import com.melalie.fooddelivery.model.dto.UserData;
import com.melalie.fooddelivery.model.entity.Menu;
import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.model.entity.User;
import com.melalie.fooddelivery.model.entity.UserPurchase;
import com.melalie.fooddelivery.repository.MenuRepository;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import com.melalie.fooddelivery.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ResetService {

    private MenuRepository menuRepository;
    private RestaurantRepository restaurantRepository;
    private UserPurchaseRepository userPurchaseRepository;
    private UserRepository userRepository;

    public ResetService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, UserPurchaseRepository userPurchaseRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.userPurchaseRepository = userPurchaseRepository;
        this.userRepository = userRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void execute() {
        log.info("Start resetting and reinsert data");

        //Delete all data existing
        deleteData();

        retrieveRestaurantData();
        retrieveUserData();
    }

    private void deleteData() {
        menuRepository.deleteAll();
        restaurantRepository.deleteAll();
        userPurchaseRepository.deleteAll();
        userRepository.deleteAll();
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

        List<Restaurant> restaurantList = new ArrayList<>();
        List<Menu> restaurantMenu = new ArrayList<>();
        restaurantData.forEach(
                data -> {
                    Restaurant restaurant = Restaurant.builder()
                            .name(data.getName())
                            .location(data.getLocation())
                            .balance(new BigDecimal(data.getBalance()))
                            .businessHours(data.getBusinessHours())
                            .build();
                    restaurantList.add(restaurant);

                    data.getMenu().forEach(
                            rawData -> {
                                Menu menu = Menu.builder()
                                        .name(rawData.getName())
                                        .price(Double.parseDouble(rawData.getPrice()))
                                        .restaurantName(data.getName())
                                        .build();

                                restaurantMenu.add(menu);
                            });

                });

        //Save all data without looping
        restaurantRepository.saveAll(restaurantList);
        menuRepository.saveAll(restaurantMenu);

        log.info("Finish inserting Restaurants data. Time finished: {} ms", (System.currentTimeMillis() - currentTime));
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
        List<User> userList = new ArrayList<>();
        List<UserPurchase> userPurchases = new ArrayList<>();

        userData.forEach(data -> {
                    User user = User.builder()
                            .name(data.getName())
                            .location(data.getLocation())
                            .balance(new BigDecimal(data.getBalance()))
                            .build();
                    userList.add(user);

                    data.getPurchases().forEach(
                            purchase -> {
                                UserPurchase userPurchase = UserPurchase.builder()
                                        .dish(purchase.getDish())
                                        .restaurantName(purchase.getRestaurantName())
                                        .amount(Double.parseDouble(purchase.getAmount()))
                                        .date(purchase.getDate())
                                        .user(data.getName())
                                        .build();
                                userPurchases.add(userPurchase);
                            }
                    );
                });

        userRepository.saveAll(userList);
        userPurchaseRepository.saveAll(userPurchases);

        log.info("Finish inserting Users data. Time finished: {} ms", (System.currentTimeMillis() - currentTime));
    }
}

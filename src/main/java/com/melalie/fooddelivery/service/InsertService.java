package com.melalie.fooddelivery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melalie.fooddelivery.model.dto.RestaurantData;
import com.melalie.fooddelivery.model.dto.UserData;
import com.melalie.fooddelivery.model.entity.*;
import com.melalie.fooddelivery.repository.*;
import com.melalie.fooddelivery.util.constant.Day;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@Service
@Transactional
public class InsertService {

    private BusinessHourRepository businessHourRepository;
    private MenuRepository menuRepository;
    private RestaurantRepository restaurantRepository;
    private UserPurchaseRepository userPurchaseRepository;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    public InsertService(BusinessHourRepository businessHourRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository, UserPurchaseRepository userPurchaseRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.businessHourRepository = businessHourRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.userPurchaseRepository = userPurchaseRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public void execute() throws Exception {
        long currentTime = System.currentTimeMillis();

        log.info("Start inserting data");

        retrieveRestaurantData();
        retrieveUserData();

        log.info("Data insertion took : {} s", Math.ceil((double) (System.currentTimeMillis() - currentTime) / 1000));
    }

    private void retrieveRestaurantData() throws Exception {
        List<RestaurantData> restaurantData;
        try {
            restaurantData = objectMapper.readValue(new File("src/main/resources/restaurants.json"), new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Failed to retrieve Restaurants data. Error: {}", (Object) ExceptionUtils.getRootCauseStackTrace(e));

            throw new Exception("Process data failed.");
        }

        long currentTime = System.currentTimeMillis();

        List<Restaurant> restaurantList = new ArrayList<>();
        List<Menu> restaurantMenu = new ArrayList<>();

        restaurantData.forEach(
                data -> {
                    // remove any restaurant in which business hours is empty / null
                    if (StringUtils.isNotEmpty(data.getBusinessHours())) {
                        String restaurantId = UUID.randomUUID().toString();

                        Restaurant restaurant = Restaurant.builder()
                                .id(restaurantId)
                                .name(data.getName())
                                .location(data.getLocation())
                                .balance(new BigDecimal(data.getBalance()))
                                .build();
                        restaurantList.add(restaurant);

                        data.getMenu().forEach(
                                rawData -> {
                                    Menu menu = Menu.builder()
                                            .name(rawData.getName())
                                            .price(new BigDecimal(rawData.getPrice()))
                                            .restaurantId(restaurantId)
                                            .build();

                                    restaurantMenu.add(menu);
                                });


                        retrieveBusinessHour(restaurantId, data.getName(), data.getBusinessHours());
                    }
                });

        //Save all data without looping
        if (!restaurantList.isEmpty() && !restaurantMenu.isEmpty()) {
            restaurantRepository.saveAll(restaurantList);
            menuRepository.saveAll(restaurantMenu);
        }

        log.info("Finish inserting {} Restaurants data. Time finished: {} s", restaurantList.size(), Math.ceil((double) (System.currentTimeMillis() - currentTime) / 1000));
    }

    private void retrieveUserData() throws Exception {
        List<UserData> userData;

        try {
            userData = objectMapper.readValue(new File("src/main/resources/users.json"), new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Failed to retrieve Restaurants data. Error: {}", (Object) ExceptionUtils.getRootCauseStackTrace(e));

            throw new Exception("Process data failed.");
        }

        long currentTime = System.currentTimeMillis();
        List<User> userList = new ArrayList<>();
        List<UserPurchase> userPurchases = new ArrayList<>();

        userData.forEach(data -> {
            String userId = UUID.randomUUID().toString();

            User user = User.builder()
                    .id(userId)
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
                                .amount(new BigDecimal(purchase.getAmount()))
                                .date(convertDate(purchase.getDate()))
                                .userId(userId)
                                .build();
                        userPurchases.add(userPurchase);
                    }
            );
        });

        if (!userList.isEmpty() && !userPurchases.isEmpty()) {
            userRepository.saveAll(userList);
            userPurchaseRepository.saveAll(userPurchases);
        }

        log.info("Finish inserting {} Users data. Time finished: {} s", userList.size(), Math.ceil((double) (System.currentTimeMillis() - currentTime) / 1000));
    }

    private Timestamp convertDate(String requestDate) {
        Date result;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
            result = sdf.parse(requestDate);
        } catch (Exception e) {
            log.error("Failed to convert date. Payload: {}", requestDate);

            result = new Date();
        }

        return new Timestamp(result.getTime());
    }

    private void retrieveBusinessHour(String restaurantId, String restaurantName, String businessHours) {
        List<BusinessHour> restBusinessHours = new ArrayList<>();

        Arrays.stream(businessHours.split("\\|")) // Split by | to retrieve each day
                .forEach(day -> {

                    //split by ": ", and remove leading n trailing spaces. Result : "2 AM - 5 PM"
                    String retrieveOpenTime = day.split(": ")[1].trim();
                    String[] time = retrieveOpenTime.split("-");

                    String fromTime = retrieveTime(time[0]);
                    String toTime = retrieveTime(time[1]);

                    BusinessHour businessHour = BusinessHour.builder()
                            .restaurantId(restaurantId)
                            .restaurantName(restaurantName)
                            .openTime(retrieveOpenTime)
                            .fromTime(fromTime)
                            .toTime(toTime)
                            .hours(getRestaurantHours(time[0], time[1]))
                            .build();

                    if (day.toLowerCase().contains("sun")) {
                        businessHour.setDay(Day.SUNDAY.name());
                    }

                    if (day.toLowerCase().contains("mon")) {
                        businessHour.setDay(Day.MONDAY.name());
                    }

                    if (day.toLowerCase().contains("tue")) {
                        businessHour.setDay(Day.TUESDAY.name());
                    }

                    if (day.toLowerCase().contains("wed")) {
                        businessHour.setDay(Day.WEDNESDAY.name());
                    }

                    if (day.toLowerCase().contains("thu")) {
                        businessHour.setDay(Day.THURSDAY.name());
                    }

                    if (day.toLowerCase().contains("fri")) {
                        businessHour.setDay(Day.FRIDAY.name());
                    }

                    if (day.toLowerCase().contains("sat")) {
                        businessHour.setDay(Day.SATURDAY.name());
                    }

                    restBusinessHours.add(businessHour);
                });

        businessHourRepository.saveAll(restBusinessHours);
    }

    private Integer getRestaurantHours(String fromTime, String toTime) {
        fromTime = StringUtils.trim(fromTime);
        toTime = StringUtils.trim(toTime);

        LocalTime fromDate;
        LocalTime toDate;

        DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("[hh:mm a]" + "[h:mm a]" + "[h a]" + "[hh a]");
        try {
            fromDate = LocalTime.parse(fromTime, defaultFormat);
            toDate = LocalTime.parse(toTime, defaultFormat);
        } catch (Exception e) {
            log.error("Failed to parse date. Error: {}", (Object) ExceptionUtils.getRootCauseStackTrace(e));

            fromDate = LocalTime.now();
            toDate = LocalTime.now();
        }
        Duration dur = Duration.between(fromDate, toDate);

        long hours = dur.toHours();
        if (hours < 0) {
            hours += 24;
        }

        return (int) hours;
    }

    private String retrieveTime(String time) {
        time = StringUtils.replace(time, ":", StringUtils.EMPTY);
        boolean timeIsPM = StringUtils.containsIgnoreCase(time, "PM");
        time = time.replaceAll("\\D+", StringUtils.EMPTY);

        if (timeIsPM) {
            int night = NumberUtils.toInt(time);

            if (time.length() < 3) {
                time = String.valueOf(night + 12);
            } else {
                time = String.valueOf(night + 1200);
            }
        }

        if (time.length() == 3) {
            time = StringUtils.leftPad(time, 4, "0");
        } else {
            time = StringUtils.leftPad(time, 2, "0");
            time = StringUtils.rightPad(time, 4, "0");
        }

        return time;
    }
}

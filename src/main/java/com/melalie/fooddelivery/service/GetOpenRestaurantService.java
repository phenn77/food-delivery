package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.Schedule;
import com.melalie.fooddelivery.model.request.OpenedRestaurantRequest;
import com.melalie.fooddelivery.model.response.OpenedRestaurantResponse;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.melalie.fooddelivery.util.constant.CommonUtils.formatDate;
import static com.melalie.fooddelivery.util.constant.Constants.*;

@Log4j2
@Service
public class GetOpenRestaurantService {

    private RestaurantRepository restaurantRepository;

    public GetOpenRestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public OpenedRestaurantResponse execute(OpenedRestaurantRequest request) throws Exception {
        String currentDate = new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date());
        String openAt = StringUtils.isBlank(request.getOpensAt()) ? currentDate : request.getOpensAt();

        return OpenedRestaurantResponse.builder()
                .restaurants(retrieveOpenedRestaurants(openAt))
                .build();
    }

    private List<Schedule> retrieveOpenedRestaurants(String openTime) throws Exception {
        var day = formatDate(openTime, YYYYMMDDHHMMSS, DAY).toUpperCase();
        var hour = formatDate(openTime, YYYYMMDDHHMMSS, HOUR);

        return restaurantRepository.getOpenedRestaurant(day, hour)
                .stream()
                .map(data -> Schedule.builder()
                        .name(data.getName())
                        .opensAt(data.getOpenTime())
                        .build())
                .collect(Collectors.toList());
    }
}

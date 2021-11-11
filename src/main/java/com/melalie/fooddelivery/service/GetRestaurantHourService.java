package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.Schedule;
import com.melalie.fooddelivery.model.request.RestaurantHourRequest;
import com.melalie.fooddelivery.model.response.OpenedRestaurantResponse;
import com.melalie.fooddelivery.repository.BusinessHourRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetRestaurantHourService {

    private BusinessHourRepository businessHourRepository;

    public GetRestaurantHourService(BusinessHourRepository businessHourRepository) {
        this.businessHourRepository = businessHourRepository;
    }

    public OpenedRestaurantResponse execute(RestaurantHourRequest request) throws Exception {
        if (!StringUtils.equalsIgnoreCase("day", request.getType()) && !StringUtils.equalsIgnoreCase("week", request.getType())) {
            log.error("Wrong type entered. Either : day OR week");

            throw new Exception("Wrong request type.");
        }

        return getData(request);
    }

    private OpenedRestaurantResponse getData(RestaurantHourRequest request) {
        List<Schedule> restaurantList;

        if (StringUtils.equalsIgnoreCase("day", request.getType())) {
            restaurantList = byDay(request.getHours());
        } else {
            restaurantList = byWeek(request.getHours());
        }

        return OpenedRestaurantResponse.builder()
                .type(request.getType())
                .openHours(request.getHours())
                .restaurants(restaurantList)
                .build();
    }

    private List<Schedule> byDay(Integer hours) {
        return businessHourRepository.getRestaurantByDay(hours)
                .stream()
                .filter(x -> x.getTotal() == 7) //retrieve restaurant in which open for x hours each day
                .map(data -> Schedule.builder()
                        .name(data.getRestaurantName())
                        .build())
                .sorted(Comparator.comparing(Schedule::getName))
                .collect(Collectors.toList());
    }

    private List<Schedule> byWeek(Integer hours) {
        return businessHourRepository.getRestaurantByWeek(hours)
                .stream()
                .filter(x -> x.getTotal() >= hours) //retrieve restaurant in which open for x hours in a week
                .map(data -> Schedule.builder()
                        .name(data.getRestaurantName())
                        .build())
                .sorted(Comparator.comparing(Schedule::getName))
                .collect(Collectors.toList());
    }
}

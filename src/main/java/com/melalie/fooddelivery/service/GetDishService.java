package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.DishRequest;
import com.melalie.fooddelivery.model.response.RestaurantsResponse;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class GetDishService {

    private RestaurantRepository restaurantRepository;

    public GetDishService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantsResponse execute(DishRequest request) {
        return RestaurantsResponse.builder()
                .dishName(request.getDishName())
                .restaurantList(retrieveRestaurants(request.getDishName()))
                .build();
    }

    private List<String> retrieveRestaurants(String dishName) {
        return restaurantRepository.getRestaurantByDish(dishName);
    }
}

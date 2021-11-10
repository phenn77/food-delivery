package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.request.DishRequest;
import com.melalie.fooddelivery.model.request.PopularRestaurantRequest;
import com.melalie.fooddelivery.model.response.PopularRestaurantResponse;
import com.melalie.fooddelivery.model.response.RestaurantsResponse;
import com.melalie.fooddelivery.model.response.TransactionByRestaurantResponse;
import com.melalie.fooddelivery.service.GetDishService;
import com.melalie.fooddelivery.service.GetPopularRestaurantService;
import com.melalie.fooddelivery.service.GetTransactionByRestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/restaurants")
public class RestaurantController {

    private GetDishService getDishService;
    private GetPopularRestaurantService getPopularRestaurantService;
    private GetTransactionByRestaurantService getTransactionByRestaurantService;

    public RestaurantController(GetDishService getDishService, GetPopularRestaurantService getPopularRestaurantService, GetTransactionByRestaurantService getTransactionByRestaurantService) {
        this.getDishService = getDishService;
        this.getPopularRestaurantService = getPopularRestaurantService;
        this.getTransactionByRestaurantService = getTransactionByRestaurantService;
    }

    @Operation(
            summary = "Retrieve Restaurants based on Dish Name"
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantsResponse getRestaurantsByDish(@Valid @RequestBody DishRequest request) {
        return getDishService.execute(request);
    }

    @Operation(
            summary = "Retrieve most popular Restaurants based on total transaction or transaction amount",
            description = "Choose between 2 parameters in which to return the most popular Restaurants"
    )
    @PostMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PopularRestaurantResponse getPopularRestaurants(@Valid @RequestBody PopularRestaurantRequest request) throws Exception {
        return getPopularRestaurantService.execute(request);
    }

    @Operation(
            summary = "Retrieve Restaurant Transaction based on ID or Name",
            description = "Will return 1 data if restaurant ID as search parameter, meanwhile, if used NAME as search parameters, it will return list of restaurant in which the name has the value (case sensitive)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByRestaurantResponse getTransactionByRestaurant(
            @RequestParam(value = "restaurantId", required = false) String restaurantId,
            @RequestParam(value = "name", required = false) String name) throws Exception {
        return getTransactionByRestaurantService.execute(restaurantId, name);
    }
}

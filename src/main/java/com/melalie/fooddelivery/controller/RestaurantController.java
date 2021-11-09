package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.response.TransactionByRestaurantResponse;
import com.melalie.fooddelivery.service.GetTransactionByRestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurants")
public class RestaurantController {

    private GetTransactionByRestaurantService getTransactionByRestaurantService;

    public RestaurantController(GetTransactionByRestaurantService getTransactionByRestaurantService) {
        this.getTransactionByRestaurantService = getTransactionByRestaurantService;
    }

    @Operation(
            summary = "Retrieve Restaurant Transaction based on ID or Name",
            description = "Will return 1 data if restaurant ID as search parameter, meanwhile, if used NAME as search parameters, it will return list of restaurant in which the name has the value (case sensitive)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByRestaurantResponse getTransactionByRestaurant(
            @RequestParam(value = "restaurantId", required = false) String restaurantId,
            @RequestParam(value = "name", required = false) String name) {
        return getTransactionByRestaurantService.execute(restaurantId, name);
    }
}

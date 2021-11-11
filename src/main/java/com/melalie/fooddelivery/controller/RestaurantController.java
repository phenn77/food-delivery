package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.request.*;
import com.melalie.fooddelivery.model.response.*;
import com.melalie.fooddelivery.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/restaurants")
public class RestaurantController {

    private GetDishService getDishService;
    private GetOpenRestaurantService getOpenRestaurantService;
    private GetPopularRestaurantService getPopularRestaurantService;
    private GetRestaurantHourService getRestaurantHourService;
    private GetTransactionByRestaurantService getTransactionByRestaurantService;
    private SearchService searchService;

    public RestaurantController(GetDishService getDishService, GetOpenRestaurantService getOpenRestaurantService, GetPopularRestaurantService getPopularRestaurantService, GetRestaurantHourService getRestaurantHourService, GetTransactionByRestaurantService getTransactionByRestaurantService, SearchService searchService) {
        this.getDishService = getDishService;
        this.getOpenRestaurantService = getOpenRestaurantService;
        this.getPopularRestaurantService = getPopularRestaurantService;
        this.getRestaurantHourService = getRestaurantHourService;
        this.getTransactionByRestaurantService = getTransactionByRestaurantService;
        this.searchService = searchService;
    }

    @Operation(
            summary = "Search for restaurants that has a dish matching search term"
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantsResponse getRestaurantsByDish(@Valid @RequestBody DishRequest request) {
        return getDishService.execute(request);
    }

    @Operation(
            summary = "The most popular restaurants by transaction volume, either by number of transactions or transaction amount",
            description = "Choose between 2 parameters in which to return the most popular Restaurants"
    )
    @PostMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PopularRestaurantResponse getPopularRestaurants(@Valid @RequestBody PopularRestaurantRequest request) throws Exception {
        return getPopularRestaurantService.execute(request);
    }

    @Operation(
            summary = "List all restaurants that are open at a certain datetime",
            description = "format should be in 'yyyy-MM-dd HH:mm:ss'. Example : 2021-11-10 14:00:00"
    )
    @PostMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OpenedRestaurantResponse getOpenedRestaurant(@Valid @RequestBody OpenedRestaurantRequest request) throws Exception {
        return getOpenRestaurantService.execute(request);
    }

    @Operation(
            summary = "List all restaurants that are open for x-z hours per day or week",
            description = "type must be either [day] or [week]"
    )
    @PostMapping(value = "/open", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OpenedRestaurantResponse getOpenedRestaurantByHour(@Valid @RequestBody RestaurantHourRequest request) throws Exception {
        return getRestaurantHourService.execute(request);
    }

    @Operation(
            summary = "List all transactions belonging to a restaurant",
            description = "Will return 1 data if restaurant ID as search parameter, meanwhile, if used NAME as search parameters, it will return list of restaurant in which the name has the value (case sensitive)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionByRestaurantResponse getTransactionByRestaurant(
            @RequestParam(value = "restaurantId", required = false) String restaurantId,
            @RequestParam(value = "name", required = false) String name) throws Exception {
        return getTransactionByRestaurantService.execute(restaurantId, name);
    }

    @Operation(
            summary = "Search for restaurants or dishes by name, ranked by relevance to search term",
            description = "type should be set either [restaurant] or [dish]"
    )
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse search(@Valid @RequestBody SearchRequest request) throws Exception {
        return searchService.execute(request);
    }
}

package com.melalie.fooddelivery.controller;

import com.melalie.fooddelivery.model.request.DishRequest;
import com.melalie.fooddelivery.model.request.PopularRestaurantRequest;
import com.melalie.fooddelivery.model.request.SearchRequest;
import com.melalie.fooddelivery.model.response.PopularRestaurantResponse;
import com.melalie.fooddelivery.model.response.RestaurantsResponse;
import com.melalie.fooddelivery.model.response.SearchResponse;
import com.melalie.fooddelivery.model.response.TransactionByRestaurantResponse;
import com.melalie.fooddelivery.service.GetDishService;
import com.melalie.fooddelivery.service.GetPopularRestaurantService;
import com.melalie.fooddelivery.service.GetTransactionByRestaurantService;
import com.melalie.fooddelivery.service.SearchService;
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
    private SearchService searchService;

    public RestaurantController(GetDishService getDishService, GetPopularRestaurantService getPopularRestaurantService, GetTransactionByRestaurantService getTransactionByRestaurantService, SearchService searchService) {
        this.getDishService = getDishService;
        this.getPopularRestaurantService = getPopularRestaurantService;
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
            description = "type should be set either 'restaurant' or 'dish'"
    )
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse search(@Valid @RequestBody SearchRequest request) throws Exception {
        return searchService.execute(request);
    }
}

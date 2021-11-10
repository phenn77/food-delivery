package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.SearchResult;
import com.melalie.fooddelivery.model.request.SearchRequest;
import com.melalie.fooddelivery.model.response.SearchResponse;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SearchService {

    private RestaurantRepository restaurantRepository;

    public SearchService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public SearchResponse execute(SearchRequest request) throws Exception {
        var searchKeyword = "%" + request.getKeyword().toLowerCase() + "%";

        if (StringUtils.equalsIgnoreCase("restaurant", request.getType())) {
            return SearchResponse.builder()
                    .result(retrieveByRestaurant(searchKeyword))
                    .build();
        }

        if (StringUtils.equalsIgnoreCase("dish", request.getType())) {
            return SearchResponse.builder()
                    .result(retrieveByDish(searchKeyword))
                    .build();
        }

        throw new Exception("Wrong type : restaurant / dish");
    }

    private List<SearchResult> retrieveByRestaurant(String keyword) {
        return restaurantRepository.searchByName(keyword)
                .stream()
                .map(data -> SearchResult.builder()
                        .restaurantId(data.getRestaurantId())
                        .restaurantName(data.getRestaurantName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SearchResult> retrieveByDish(String keyword) {
        return restaurantRepository.searchByDish(keyword)
                .stream()
                .map(data -> SearchResult.builder()
                        .restaurantId(data.getRestaurantId())
                        .restaurantName(data.getRestaurantName())
                        .dish(data.getDishName())
                        .build())
                .collect(Collectors.toList());
    }
}

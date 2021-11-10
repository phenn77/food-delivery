package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.DishRequest;
import com.melalie.fooddelivery.model.response.RestaurantsResponse;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetDishServiceTest {

    @InjectMocks
    private GetDishService getDishService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    private static final String DISH_NAME = "Kiwi";

    private static final DishRequest REQUEST = DishRequest.builder()
            .dishName(DISH_NAME)
            .build();

    @Test
    public void getDish_EmptyData_Test() {
        when(restaurantRepository.getRestaurantByDish(DISH_NAME))
                .thenReturn(Collections.emptyList());

        var response = getDishService.execute(REQUEST);
        assertEquals(DISH_NAME, response.getDishName());
        assertEquals(0, response.getRestaurantList().size());

        verify(restaurantRepository).getRestaurantByDish(DISH_NAME);
    }

    @Test
    public void getDish_WithData_Test() {
        final String REST_1 = "Orange House";
        final String REST_2 = "Blue Plate Creamery";

        when(restaurantRepository.getRestaurantByDish(DISH_NAME))
                .thenReturn(List.of(REST_1, REST_2));

        var response = getDishService.execute(REQUEST);
        assertEquals(DISH_NAME, response.getDishName());
        assertEquals(2, response.getRestaurantList().size());
        assertEquals(REST_1, response.getRestaurantList().get(0));
        assertEquals(REST_2, response.getRestaurantList().get(1));

        verify(restaurantRepository).getRestaurantByDish(DISH_NAME);
    }
}

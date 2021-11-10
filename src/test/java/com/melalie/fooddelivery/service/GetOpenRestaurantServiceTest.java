package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.RestaurantSchedule;
import com.melalie.fooddelivery.model.request.OpenedRestaurantRequest;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetOpenRestaurantServiceTest {

    @InjectMocks
    private GetOpenRestaurantService getOpenRestaurantService;

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

    private static final OpenedRestaurantRequest request = OpenedRestaurantRequest.builder()
            .opensAt("2021-11-10 17:00:00") // WEDNESDAY
            .build();

    @Test
    public void getOpenRestaurant_EmptyData_Test() throws Exception {
        when(restaurantRepository.getOpenedRestaurant("WEDNESDAY", "1700"))
                .thenReturn(Collections.emptyList());

        var response = getOpenRestaurantService.execute(request);
        assertEquals(0, response.getRestaurants().size());

        verify(restaurantRepository).getOpenedRestaurant("WEDNESDAY", "1700");
    }

    @Test
    public void getOpenRestaurant_WithData_Test() throws Exception {
        var request = OpenedRestaurantRequest.builder()
                .opensAt("2021-11-10 17:00:00") // WEDNESDAY
                .build();

        RestaurantSchedule schedule = new RestaurantSchedule() {
            @Override
            public String getName() {
                return "Orange";
            }

            @Override
            public String getOpenTime() {
                return "12PM - 6PM";
            }
        };

        when(restaurantRepository.getOpenedRestaurant("WEDNESDAY", "1700"))
                .thenReturn(Collections.singletonList(schedule));

        var response = getOpenRestaurantService.execute(request);
        assertEquals(1, response.getRestaurants().size());
        assertEquals("Orange", response.getRestaurants().get(0).getName());
        assertEquals("12PM - 6PM", response.getRestaurants().get(0).getOpensAt());

        verify(restaurantRepository).getOpenedRestaurant("WEDNESDAY", "1700");

    }
}

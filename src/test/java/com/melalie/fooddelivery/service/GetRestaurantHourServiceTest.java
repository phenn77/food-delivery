package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.OpenedRestaurant;
import com.melalie.fooddelivery.model.request.RestaurantHourRequest;
import com.melalie.fooddelivery.repository.BusinessHourRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetRestaurantHourServiceTest {

    @InjectMocks
    private GetRestaurantHourService getRestaurantHourService;

    @Mock
    private BusinessHourRepository businessHourRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(businessHourRepository);
    }

    @Test
    public void getRestaurant_WrongType_Test() {
        var request = RestaurantHourRequest.builder()
                .type("year")
                .build();

        var thrown = assertThrows(Exception.class, () ->
                getRestaurantHourService.execute(request)
        );
        assertEquals("Wrong request type.", thrown.getMessage());
    }

    @Test
    public void getRestaurant_ByDay_Test() throws Exception {
        var request = RestaurantHourRequest.builder()
                .type("day")
                .hours(5)
                .build();

        OpenedRestaurant restaurant = new OpenedRestaurant() {
            @Override
            public String getRestaurantId() {
                return null;
            }

            @Override
            public String getRestaurantName() {
                return "orange house";
            }

            @Override
            public Integer getTotal() {
                return 4;
            }
        };

        OpenedRestaurant restaurant_2 = new OpenedRestaurant() {
            @Override
            public String getRestaurantId() {
                return null;
            }

            @Override
            public String getRestaurantName() {
                return "Steak";
            }

            @Override
            public Integer getTotal() {
                return 7;
            }
        };

        when(businessHourRepository.getRestaurantByDay(5))
                .thenReturn(List.of(restaurant, restaurant_2));

        var response = getRestaurantHourService.execute(request);
        assertEquals(1, response.getRestaurants().size());
        assertEquals("Steak", response.getRestaurants().get(0).getName());

        verify(businessHourRepository).getRestaurantByDay(5);
    }

    @Test
    public void getRestaurant_ByWeek_Test() throws Exception {
        var request = RestaurantHourRequest.builder()
                .type("week")
                .hours(10)
                .build();

        OpenedRestaurant restaurant = new OpenedRestaurant() {
            @Override
            public String getRestaurantId() {
                return null;
            }

            @Override
            public String getRestaurantName() {
                return "orange house";
            }

            @Override
            public Integer getTotal() {
                return 10;
            }
        };

        OpenedRestaurant restaurant_2 = new OpenedRestaurant() {
            @Override
            public String getRestaurantId() {
                return null;
            }

            @Override
            public String getRestaurantName() {
                return "Steak";
            }

            @Override
            public Integer getTotal() {
                return 7;
            }
        };

        when(businessHourRepository.getRestaurantByWeek(10))
                .thenReturn(List.of(restaurant, restaurant_2));

        var response = getRestaurantHourService.execute(request);
        assertEquals(1, response.getRestaurants().size());
        assertEquals("orange house", response.getRestaurants().get(0).getName());

        verify(businessHourRepository).getRestaurantByWeek(10);
    }
}

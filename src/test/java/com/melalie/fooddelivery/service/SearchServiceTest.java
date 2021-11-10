package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.SearchData;
import com.melalie.fooddelivery.model.request.SearchRequest;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import org.apache.commons.lang3.StringUtils;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

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

    @Test
    public void search_WrongType_Test() {
        var request = SearchRequest.builder()
                .type(StringUtils.EMPTY)
                .keyword("search")
                .build();

        var thrown = assertThrows(Exception.class, () -> {
            searchService.execute(request);
        });
        assertEquals("Wrong type : restaurant / dish", thrown.getMessage());
    }

    @Test
    public void search_ByRestaurant_Test() throws Exception {
        var request = SearchRequest.builder()
                .type("restaurant")
                .keyword("orange")
                .build();

        SearchData data = new SearchData() {
            @Override
            public String getRestaurantId() {
                return "id";
            }

            @Override
            public String getRestaurantName() {
                return "orange house";
            }

            @Override
            public String getDishName() {
                return null;
            }
        };

        when(restaurantRepository.searchByName(anyString()))
                .thenReturn(List.of(data));

        var response = searchService.execute(request);
        assertEquals(1, response.getResult().size());

        verify(restaurantRepository).searchByName(anyString());
    }

    @Test
    public void search_ByDish_Test() throws Exception {
        var request = SearchRequest.builder()
                .type("dish")
                .keyword("orange")
                .build();

        SearchData data = new SearchData() {
            @Override
            public String getRestaurantId() {
                return "id";
            }

            @Override
            public String getRestaurantName() {
                return "orange house";
            }

            @Override
            public String getDishName() {
                return "orange dish";
            }
        };

        when(restaurantRepository.searchByDish(anyString()))
                .thenReturn(List.of(data));

        var response = searchService.execute(request);
        assertEquals(1, response.getResult().size());

        verify(restaurantRepository).searchByDish(anyString());
    }
}

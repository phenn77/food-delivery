package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.RestaurantTransactionData;
import com.melalie.fooddelivery.model.response.TransactionByRestaurantResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetTransactionByRestaurantServiceTest {

    @InjectMocks
    private GetTransactionByRestaurantService getTransactionByRestaurantService;

    @Mock
    private UserPurchaseRepository userPurchaseRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userPurchaseRepository);
    }

    private static final String RESTAURANT_ID_1 = UUID.randomUUID().toString();
    private static final String RESTAURANT_ID_2 = UUID.randomUUID().toString();
    private static final String RESTAURANT_ID_3 = UUID.randomUUID().toString();
    private static final String RESTAURANT_NAME_1 = "Orange House";
    private static final String RESTAURANT_NAME_2 = "Orange House";
    private static final String RESTAURANT_NAME_3 = "Orange House";
    private static final String DISH_1 = "Spaghetti";
    private static final String DISH_2 = "Steak";
    private static final double AMT_1 = 10.00;
    private static final double AMT_2 = 3.00;

    private static RestaurantTransactionData RESTAURANT_DATA(
            String restaurantId,
            String restaurantName,
            String dishName,
            double amount) {
        return new RestaurantTransactionData() {
            @Override
            public String getRestaurantId() {
                return restaurantId;
            }

            @Override
            public String getRestaurantName() {
                return restaurantName;
            }

            @Override
            public String getDish() {
                return dishName;
            }

            @Override
            public double getAmount() {
                return amount;
            }
        };
    }

    private static final RestaurantTransactionData REST_1 = RESTAURANT_DATA(RESTAURANT_ID_1, RESTAURANT_NAME_1, DISH_1,AMT_1);
    private static final RestaurantTransactionData REST_2 = RESTAURANT_DATA(RESTAURANT_ID_2, RESTAURANT_NAME_2, DISH_2, AMT_2);
    private static final RestaurantTransactionData REST_3 = RESTAURANT_DATA(RESTAURANT_ID_3, RESTAURANT_NAME_3, DISH_2, AMT_1);

    @Test
    public void getTransaction_RequestNotComplete_Test() {
        TransactionByRestaurantResponse response = getTransactionByRestaurantService.execute(null, null);
        assertNull(response);
    }

    @Test
    public void getTransaction_ByName_Test() {
        when(userPurchaseRepository.retrieveRestaurantTransactionsByName(RESTAURANT_NAME_1))
                .thenReturn(List.of(REST_1, REST_2, REST_3));

        Map<String, Integer> rest1 = Map.of(DISH_1, 1);
        Map<String, Integer> rest2 = Map.of(DISH_2, 1);
        Map<String, Integer> rest3 = Map.of(DISH_2, 1);
        TransactionByRestaurantResponse response = getTransactionByRestaurantService.execute(null, RESTAURANT_NAME_1);
        assertEquals(3, response.getRestaurantsList().size());
        assertEquals(rest1, response.getRestaurantsList().get(0).getTransactionList());
        assertEquals(rest2, response.getRestaurantsList().get(1).getTransactionList());
        assertEquals(rest3, response.getRestaurantsList().get(2).getTransactionList());

        verify(userPurchaseRepository).retrieveRestaurantTransactionsByName(RESTAURANT_NAME_1);
    }

    @Test
    public void getTransaction_ById_Test() {
        when(userPurchaseRepository.retrieveRestaurantTransactions(RESTAURANT_ID_1))
                .thenReturn(List.of(REST_1, REST_2, REST_1));

        Map<String, Integer> rest1 = Map.of(DISH_1, 2);
        Map<String, Integer> rest2 = Map.of(DISH_2, 1);
        TransactionByRestaurantResponse response = getTransactionByRestaurantService.execute(RESTAURANT_ID_1, null);
        assertEquals(2, response.getRestaurantsList().size());
        assertEquals(rest1, response.getRestaurantsList().get(0).getTransactionList());
        assertEquals(rest2, response.getRestaurantsList().get(1).getTransactionList());

        verify(userPurchaseRepository).retrieveRestaurantTransactions(RESTAURANT_ID_1);
    }

    @Test
    public void getTransaction_EmptyData_Test() {
        when(userPurchaseRepository.retrieveRestaurantTransactionsByName(RESTAURANT_NAME_1))
                .thenReturn(Collections.emptyList());

        TransactionByRestaurantResponse response = getTransactionByRestaurantService.execute(null, RESTAURANT_NAME_1);
        assertEquals(0, response.getRestaurantsList().size());

        verify(userPurchaseRepository).retrieveRestaurantTransactionsByName(RESTAURANT_NAME_1);
    }
}

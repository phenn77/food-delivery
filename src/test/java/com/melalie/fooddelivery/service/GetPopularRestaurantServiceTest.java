package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.RestaurantTransactionData;
import com.melalie.fooddelivery.model.request.PopularRestaurantRequest;
import com.melalie.fooddelivery.model.response.PopularRestaurantResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetPopularRestaurantServiceTest {

    @InjectMocks
    private GetPopularRestaurantService getPopularRestaurantService;

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

    @Test
    public void getRestaurant_RequestEmpty_Test() {
        final PopularRestaurantRequest request = PopularRestaurantRequest.builder()
                .build();

        var thrown = assertThrows(Exception.class, () ->
                getPopularRestaurantService.execute(request)
        );
        assertEquals("Payload not complete.", thrown.getMessage());
    }

    @Test
    public void getRestaurant_ByTransaction_EmptyData_Test() throws Exception {
        final PopularRestaurantRequest request = PopularRestaurantRequest.builder()
                .byTransaction(true)
                .build();

        when(userPurchaseRepository.retrieveTransactions())
                .thenReturn(Collections.emptyList());

        PopularRestaurantResponse response = getPopularRestaurantService.execute(request);
        assertEquals(0, response.getRestaurantList().size());

        verify(userPurchaseRepository).retrieveTransactions();
    }

    @Test
    public void getRestaurant_ByAmount_Test() throws Exception {
        final String NAME_1 = "338 Cafe";
        final String NAME_2 = "Orange Cafe";

        final String ID_1 = UUID.randomUUID().toString();
        final String ID_2 = UUID.randomUUID().toString();

        final double AMT_1 = 10.00;
        final double AMT_2 = 30.00;

        final RestaurantTransactionData REST_DATA = new RestaurantTransactionData() {
            @Override
            public String getRestaurantId() {
                return ID_1;
            }

            @Override
            public String getRestaurantName() {
                return NAME_1;
            }

            @Override
            public String getDish() {
                return null;
            }

            @Override
            public double getAmount() {
                return AMT_1;
            }
        };

        final RestaurantTransactionData REST_DATA_2 = new RestaurantTransactionData() {
            @Override
            public String getRestaurantId() {
                return ID_2;
            }

            @Override
            public String getRestaurantName() {
                return NAME_2;
            }

            @Override
            public String getDish() {
                return null;
            }

            @Override
            public double getAmount() {
                return AMT_2;
            }
        };

        final RestaurantTransactionData REST_DATA_3 = new RestaurantTransactionData() {
            @Override
            public String getRestaurantId() {
                return ID_2;
            }

            @Override
            public String getRestaurantName() {
                return NAME_2;
            }

            @Override
            public String getDish() {
                return null;
            }

            @Override
            public double getAmount() {
                return AMT_2;
            }
        };

        final PopularRestaurantRequest request = PopularRestaurantRequest.builder()
                .byAmount(true)
                .build();

        when(userPurchaseRepository.retrieveTransactions())
                .thenReturn(List.of(REST_DATA, REST_DATA_2, REST_DATA_3));

        PopularRestaurantResponse response = getPopularRestaurantService.execute(request);
        assertEquals(2, response.getRestaurantList().size());
        assertEquals(NAME_2, response.getRestaurantList().get(0).getName());
        assertEquals(2, response.getRestaurantList().get(0).getNumberOfTransactions());
        assertEquals(new BigDecimal(60).setScale(2, RoundingMode.DOWN), response.getRestaurantList().get(0).getTotalAmount());
        assertEquals(NAME_1, response.getRestaurantList().get(1).getName());
        assertEquals(1, response.getRestaurantList().get(1).getNumberOfTransactions());
        assertEquals(new BigDecimal(AMT_1).setScale(2, RoundingMode.DOWN), response.getRestaurantList().get(1).getTotalAmount());

        verify(userPurchaseRepository).retrieveTransactions();
    }
}

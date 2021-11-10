package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import com.melalie.fooddelivery.model.response.TransactionByUserResponse;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetTransactionByUserServiceTest {

    @InjectMocks
    private GetTransactionByUserService getTransactionsByUserService;

    @Mock
    private UserPurchaseRepository userPurchaseRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(
                userPurchaseRepository
        );
    }

    private static final String USER_ID = "8c04efff-cea0-4ad5-af33-1b738de94373";
    private static final String NAME = "Charles";

    private UserPurchaseData createUserData(
            String dish,
            String restaurantName,
            double amount,
            Timestamp date) {
        return new UserPurchaseData() {
            @Override
            public String getDish() {
                return dish;
            }

            @Override
            public String getRestaurantName() {
                return restaurantName;
            }

            @Override
            public double getAmount() {
                return amount;
            }

            @Override
            public Timestamp getDate() {
                return date;
            }

            @Override
            public String getName() {
                return NAME;
            }

            @Override
            public String getUserId() {
                return USER_ID;
            }
        };
    }

    @Test
    public void getTransaction_EmptyParams_Test() {
        var thrown = assertThrows(Exception.class, () ->
                getTransactionsByUserService.execute(null, null)
        );
        assertEquals("Payload not complete.", thrown.getMessage());
    }

    @Test
    public void getTransaction_UserNotFound_Test() {
        when(userPurchaseRepository.findByUserName(anyString()))
                .thenReturn(Collections.emptyList());


        var thrown = assertThrows(Exception.class, () ->
                getTransactionsByUserService.execute(StringUtils.EMPTY, NAME)
        );
        assertEquals("User not found.", thrown.getMessage());

        verify(userPurchaseRepository).findByUserName(anyString());
    }

    @Test
    public void getTransaction_Success_Test() throws Exception {
        final String REST_1 = "Orange House";
        final String REST_2 = "34 Grill & Tap";
        final String REST_3 = "76 King";

        final String DISH_1 = "Spaghetti Carbonara";
        final String DISH_2 = "Spaghetti Aglio Olio";
        final String DISH_3 = "Tomahawk Steak";

        final double AMOUNT_1 = 10.00;
        final double AMOUNT_2 = 15.00;
        final double AMOUNT_3 = 20.00;

        final Timestamp TIME_1 = Timestamp.valueOf("2021-08-20 12:05:10");
        final Timestamp TIME_2 = Timestamp.valueOf("2021-09-31 10:09:43");
        final Timestamp TIME_3 = Timestamp.valueOf("2019-10-08 17:10:59");

        final UserPurchaseData USER_PURCHASE_1 = createUserData(DISH_1, REST_1, AMOUNT_1, TIME_1);
        final UserPurchaseData USER_PURCHASE_2 = createUserData(DISH_2, REST_2, AMOUNT_2, TIME_2);
        final UserPurchaseData USER_PURCHASE_3 = createUserData(DISH_3, REST_3, AMOUNT_3, TIME_3);

        when(userPurchaseRepository.findByUserId(USER_ID))
                .thenReturn(Arrays.asList(USER_PURCHASE_1, USER_PURCHASE_2, USER_PURCHASE_3));

        TransactionByUserResponse response = getTransactionsByUserService.execute(USER_ID, StringUtils.EMPTY);
        assertEquals(NAME, response.getUsers().get(0).getName());
        assertEquals(new BigDecimal("45.00"), response.getUsers().get(0).getTotalSpent());
        assertEquals(3, response.getUsers().get(0).getUserPurchases().size());
        assertEquals(DISH_2, response.getUsers().get(0).getUserPurchases().get(0).getDish());
        assertEquals(DISH_1, response.getUsers().get(0).getUserPurchases().get(1).getDish());
        assertEquals(DISH_3, response.getUsers().get(0).getUserPurchases().get(2).getDish());

        verify(userPurchaseRepository).findByUserId(USER_ID);
    }
}

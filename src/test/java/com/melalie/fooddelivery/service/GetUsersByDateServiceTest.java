package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.request.PopularRestaurantRequest;
import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.model.response.UserByDateResponse;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetUsersByDateServiceTest {

    @InjectMocks
    private GetUsersByDateService getUsersByDateService;

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
    public void getUsers_RequestNotComplete_Test() throws Exception {
        final UserRequest request = UserRequest.builder()
                .build();

        var thrown = assertThrows(Exception.class, () ->
                getUsersByDateService.execute(request)
        );
        assertEquals("Payload not complete.", thrown.getMessage());
    }

    @Test
    public void getUsers_ParseDateFailed_Test() {
        final UserRequest request = UserRequest.builder()
                .transactionAmount(BigDecimal.TEN)
                .fromDate("2021/09/22")
                .toDate("2021/05/6")
                .build();

        var thrown = assertThrows(Exception.class, () ->
                getUsersByDateService.execute(request)
        );
        assertEquals("Error parsing date.", thrown.getMessage());
    }

    @Test
    public void getUsers_Success_Test() throws Exception {
        final UserRequest request = UserRequest.builder()
                .transactionAmount(BigDecimal.TEN)
                .fromDate("2021-09-22")
                .toDate("2021-05-06")
                .build();

        when(userPurchaseRepository.getMinTransaction(anyString(), anyString(), any()))
                .thenReturn(0);
        when(userPurchaseRepository.getMaxTransaction(anyString(), anyString(), any()))
                .thenReturn(1);

        Map<String, Integer> result = Map.of(
                "Below", 0,
                "Over", 1
        );
        UserByDateResponse response = getUsersByDateService.execute(request);
        assertEquals(BigDecimal.TEN, response.getTransactionAmount());
        assertEquals(result, response.getTotalUsers());

        verify(userPurchaseRepository).getMinTransaction(anyString(), anyString(), any());
        verify(userPurchaseRepository).getMaxTransaction(anyString(), anyString(), any());
    }
}

package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.UserTransaction;
import com.melalie.fooddelivery.model.request.UserRequest;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetTopUsersServiceTest {

    @InjectMocks
    private GetTopUsersService getTopUsersService;

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
    public void getTopUsers_RequestNotComplete_Test() {
        var request = UserRequest.builder()
                .fromDate("2021-09-10")
                .toDate("2021-09-11")
                .build();

        var thrown = assertThrows(Exception.class, () ->
                getTopUsersService.execute(request)
        );
        assertEquals("Payload not complete.", thrown.getMessage());
    }

    @Test
    public void getTopUsers_EmptyData_Test() throws Exception {
        var request = UserRequest.builder()
                .fromDate("2021-09-10")
                .toDate("2021-09-11")
                .totalUser(10)
                .build();

        when(userPurchaseRepository.getUsersTransaction(anyString(), anyString(), anyInt()))
                .thenReturn(Collections.emptyList());

        var response = getTopUsersService.execute(request);
        assertEquals(0, response.getUsers().size());

        verify(userPurchaseRepository).getUsersTransaction(anyString(), anyString(), anyInt());
    }

    @Test
    public void getTopUsers_Success_Test() throws Exception {
        var request = UserRequest.builder()
                .fromDate("2021-09-10")
                .toDate("2021-09-11")
                .totalUser(10)
                .build();

        var charlie = "Charlie";
        var tim = "Tim";

        UserTransaction CHARLIE = new UserTransaction() {
            @Override
            public String getName() {
                return charlie;
            }

            @Override
            public Integer getTotalTransaction() {
                return 1;
            }

            @Override
            public BigDecimal getTotalAmount() {
                return BigDecimal.TEN;
            }
        };

        UserTransaction TIM = new UserTransaction() {
            @Override
            public String getName() {
                return tim;
            }

            @Override
            public Integer getTotalTransaction() {
                return 10;
            }

            @Override
            public BigDecimal getTotalAmount() {
                return BigDecimal.ONE;
            }
        };

        when(userPurchaseRepository.getUsersTransaction(anyString(), anyString(), anyInt()))
                .thenReturn(List.of(TIM, CHARLIE));

        var response = getTopUsersService.execute(request);
        assertEquals(2, response.getUsers().size());

        verify(userPurchaseRepository).getUsersTransaction(anyString(), anyString(), anyInt());
    }
}

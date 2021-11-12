package com.melalie.fooddelivery.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melalie.fooddelivery.model.dto.RestaurantData;
import com.melalie.fooddelivery.model.dto.UserData;
import com.melalie.fooddelivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class InsertServiceTest {

    @InjectMocks
    private InsertService resetService;

    @Mock
    private BusinessHourRepository businessHourRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserPurchaseRepository userPurchaseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void insert_EmptyData_Test() throws IOException {
        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> resetService.execute());

        verify(objectMapper, times(2)).readValue(any(File.class), any(TypeReference.class));
    }

    @Test
    public void insert_RestaurantDataError_Test() throws IOException {
        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenThrow(new IndexOutOfBoundsException());

        var thrown = assertThrows(Exception.class, () -> resetService.execute());
        assertEquals("Process data failed.", thrown.getMessage());

        verify(objectMapper).readValue(any(File.class), any(TypeReference.class));
    }

    @Test
    public void insert_UserDataError_Test() throws IOException {
        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenReturn(Collections.emptyList())
                .thenThrow(new IndexOutOfBoundsException());

        var thrown = assertThrows(Exception.class, () -> resetService.execute());
        assertEquals("Process data failed.", thrown.getMessage());

        verify(objectMapper, times(2)).readValue(any(File.class), any(TypeReference.class));
    }

    @Test
    public void insert_WithData_Test() throws IOException {
        RestaurantData rd = RestaurantData.builder()
                .balance("1200.0")
                .businessHours("Sun: 1:30 PM - 11:45 PM | Mon, Tue, Sat: 6:30 AM - 9:15 AM | Wed: 12:30 PM - 2:45 PM | Thu: 12:45 PM - 2 AM | Fri: 2:45 PM - 10 PM")
                .menu(Collections.singletonList(
                        RestaurantData.MenuData.builder()
                                .price("1000")
                                .build())
                )
                .build();
        UserData ud = UserData.builder()
                .balance("1111")
                .purchases(Collections.singletonList(
                        UserData.UserPurchaseData.builder()
                                .amount("4220.90")
                                .build())
                )
                .build();

        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenReturn(Collections.singletonList(rd))
                .thenReturn(Collections.singletonList(ud));

        assertDoesNotThrow(() -> resetService.execute());

        verify(objectMapper, times(2)).readValue(any(File.class), any(TypeReference.class));
        verify(businessHourRepository).saveAll(any());
        verify(restaurantRepository).saveAll(any());
        verify(menuRepository).saveAll(any());
        verify(userRepository).saveAll(any());
        verify(userPurchaseRepository).saveAll(any());
    }

    @Test
    public void insert_WithDataDateError_Test() throws IOException {
        RestaurantData rd = RestaurantData.builder()
                .balance("1200.0")
                .businessHours("Sun: 1:30:00 PM - 11:45:00 PM")
                .menu(Collections.singletonList(
                        RestaurantData.MenuData.builder()
                                .price("1000")
                                .build())
                )
                .build();
        UserData ud = UserData.builder()
                .balance("1111")
                .purchases(Collections.singletonList(
                        UserData.UserPurchaseData.builder()
                                .amount("4220.90")
                                .build())
                )
                .build();

        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenReturn(Collections.singletonList(rd))
                .thenReturn(Collections.singletonList(ud));

        assertDoesNotThrow(() -> resetService.execute());

        verify(objectMapper, times(2)).readValue(any(File.class), any(TypeReference.class));
        verify(businessHourRepository).saveAll(any());
        verify(restaurantRepository).saveAll(any());
        verify(menuRepository).saveAll(any());
        verify(userRepository).saveAll(any());
        verify(userPurchaseRepository).saveAll(any());
    }

    @Test
    public void insert_UserDateError_Test() throws IOException {
        RestaurantData rd = RestaurantData.builder()
                .balance("1200.0")
                .businessHours("Sun: 1:30:00 PM - 11:45:00 PM")
                .menu(Collections.singletonList(
                        RestaurantData.MenuData.builder()
                                .price("1000")
                                .build())
                )
                .build();
        UserData ud = UserData.builder()
                .balance("1111")
                .purchases(Collections.singletonList(
                        UserData.UserPurchaseData.builder()
                                .amount("4220.90")
                                .date("5 Sept 2021 12:00:00")
                                .build())
                )
                .build();

        when(objectMapper.readValue(any(File.class), any(TypeReference.class)))
                .thenReturn(Collections.singletonList(rd))
                .thenReturn(Collections.singletonList(ud));

        assertDoesNotThrow(() -> resetService.execute());

        verify(objectMapper, times(2)).readValue(any(File.class), any(TypeReference.class));
        verify(businessHourRepository).saveAll(any());
        verify(restaurantRepository).saveAll(any());
        verify(menuRepository).saveAll(any());
        verify(userRepository).saveAll(any());
        verify(userPurchaseRepository).saveAll(any());
    }
}

package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.entity.Menu;
import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.model.entity.User;
import com.melalie.fooddelivery.model.request.CreatePurchaseRequest;
import com.melalie.fooddelivery.model.request.MenuPurchaseRequest;
import com.melalie.fooddelivery.repository.MenuRepository;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import com.melalie.fooddelivery.repository.UserRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class CreatePurchaseServiceTest {

    @InjectMocks
    private CreatePurchaseService createPurchaseService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPurchaseRepository userPurchaseRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(
                menuRepository,
                restaurantRepository,
                userRepository,
                userPurchaseRepository
        );
    }

    final static String ERR_MSG = "Data not found.";

    final static String USER_ID = "111";
    final static String REST_ID = "222";
    final static String MENU_ID = "123";
    final static String CHARLES = "Charles";

    final static MenuPurchaseRequest MENU_REQ = MenuPurchaseRequest.builder()
            .menuId(MENU_ID)
            .quantity(2)
            .build();

    final static CreatePurchaseRequest REQUEST = CreatePurchaseRequest.builder()
            .userId(USER_ID)
            .restaurantId(REST_ID)
            .purchases(Collections.singletonList(MENU_REQ))
            .build();

    final static User USER = User.builder()
            .name(CHARLES)
            .build();

    final static Restaurant RESTAURANT = Restaurant.builder()
            .balance(BigDecimal.TEN)
            .build();

    final static Menu MENU = Menu.builder()
            .id(MENU_ID)
            .name("Steak")
            .price(12.00)
            .build();

    @Test
    public void create_UserNotFound_Test() {
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(restaurantRepository.findById(anyString()))
                .thenReturn(Optional.of(RESTAURANT));
        when(menuRepository.findByRestaurant(anyString()))
                .thenReturn(Collections.singletonList(MENU));

        var thrown = assertThrows(
                Exception.class, () -> createPurchaseService.execute(REQUEST)
        );
        assertEquals(ERR_MSG, thrown.getMessage());

        verify(userRepository).findById(anyString());
        verify(restaurantRepository).findById(anyString());
        verify(menuRepository).findByRestaurant(anyString());
    }

    @Test
    public void create_RestaurantNotFound_Test() {
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(USER));
        when(restaurantRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        when(menuRepository.findByRestaurant(anyString()))
                .thenReturn(Collections.emptyList());

        var thrown = assertThrows(
                Exception.class, () -> createPurchaseService.execute(REQUEST)
        );
        assertEquals(ERR_MSG, thrown.getMessage());

        verify(userRepository).findById(anyString());
        verify(restaurantRepository).findById(anyString());
        verify(menuRepository).findByRestaurant(anyString());
    }

    @Test
    public void create_MenuNotFound_Test() {
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(USER));
        when(restaurantRepository.findById(anyString()))
                .thenReturn(Optional.of(RESTAURANT));
        when(menuRepository.findByRestaurant(anyString()))
                .thenReturn(Collections.emptyList());

        var thrown = assertThrows(
                Exception.class, () -> createPurchaseService.execute(REQUEST)
        );
        assertEquals(ERR_MSG, thrown.getMessage());

        verify(userRepository).findById(anyString());
        verify(restaurantRepository).findById(anyString());
        verify(menuRepository).findByRestaurant(anyString());
    }

    @Test
    public void create_Success_Test() throws Exception {
        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(USER));
        when(restaurantRepository.findById(anyString()))
                .thenReturn(Optional.of(RESTAURANT));
        when(menuRepository.findByRestaurant(anyString()))
                .thenReturn(Collections.singletonList(MENU));

        var response = createPurchaseService.execute(REQUEST);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(CHARLES, response.getUser());
        assertEquals(BigDecimal.TEN, response.getPreviousBalance());
        assertEquals(new BigDecimal("34").setScale(2, RoundingMode.DOWN), response.getCurrentBalance());
        assertEquals(1, response.getPurchases().size());
        assertEquals("Steak", response.getPurchases().get(0).getName());
        assertEquals(2, response.getPurchases().get(0).getQuantity());
        assertEquals(new BigDecimal("24").setScale(2, RoundingMode.DOWN), response.getPurchases().get(0).getAmount());

        verify(userRepository).findById(anyString());
        verify(restaurantRepository).findById(anyString());
        verify(menuRepository).findByRestaurant(anyString());
        verify(userPurchaseRepository).saveAll(any());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    public void create_Failed_Test() {
        final MenuPurchaseRequest MENU_REQ = MenuPurchaseRequest.builder()
                .menuId("111")
                .quantity(2)
                .build();
        REQUEST.setPurchases(Collections.singletonList(MENU_REQ));

        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(USER));
        when(restaurantRepository.findById(anyString()))
                .thenReturn(Optional.of(RESTAURANT));
        when(menuRepository.findByRestaurant(anyString()))
                .thenReturn(Collections.singletonList(MENU));

        var thrown = assertThrows(
                Exception.class, () -> createPurchaseService.execute(REQUEST)
        );
        assertEquals("Menu not found.", thrown.getMessage());

        verify(userRepository).findById(anyString());
        verify(restaurantRepository).findById(anyString());
        verify(menuRepository).findByRestaurant(anyString());
    }
}

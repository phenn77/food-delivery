package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.projection.RestaurantAndMenu;
import com.melalie.fooddelivery.model.request.RestaurantDishesRequest;
import com.melalie.fooddelivery.repository.MenuRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class GetRestaurantDishesServiceTest {

    @InjectMocks
    private GetRestaurantDishesService getRestaurantDishesService;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(menuRepository);
    }

    private static final RestaurantDishesRequest REQUEST = RestaurantDishesRequest.builder()
            .priceFrom(BigDecimal.ONE)
            .priceTo(BigDecimal.TEN)
            .totalDishFrom(1)
            .totalDishTo(2)
            .build();

    @Test
    public void getDishes_EmptyData_Test() {
        when(menuRepository.getData(any(), any()))
                .thenReturn(Collections.emptyList());

        var response = getRestaurantDishesService.execute(REQUEST);
        assertEquals(0, response.getRestaurantList().size());

        verify(menuRepository).getData(any(), any());
    }

    @Test
    public void getDishes_DifferentRestaurant_Test() {
        final RestaurantAndMenu menu1 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return UUID.randomUUID().toString();
            }

            @Override
            public String getRestaurantName() {
                return "Lime Me";
            }

            @Override
            public String getDishName() {
                return "Steak";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        final RestaurantAndMenu menu2 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return UUID.randomUUID().toString();
            }

            @Override
            public String getRestaurantName() {
                return "Orange House";
            }

            @Override
            public String getDishName() {
                return "Spaghetti";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        when(menuRepository.getData(any(), any()))
                .thenReturn(List.of(menu1, menu2));

        var response = getRestaurantDishesService.execute(REQUEST);
        assertEquals(2, response.getRestaurantList().size());
        assertEquals("Lime Me", response.getRestaurantList().get(0).getRestaurantName());
        assertEquals(1, response.getRestaurantList().get(0).getMenuQuantity());
        assertEquals(1, response.getRestaurantList().get(0).getMenuList().size());

        assertEquals("Orange House", response.getRestaurantList().get(1).getRestaurantName());
        assertEquals(1, response.getRestaurantList().get(1).getMenuQuantity());
        assertEquals(1, response.getRestaurantList().get(1).getMenuList().size());

        verify(menuRepository).getData(any(), any());
    }

    @Test
    public void getDishes_SameRestaurant_Test() {
        final String ID = UUID.randomUUID().toString();

        final RestaurantAndMenu menu1 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return ID;
            }

            @Override
            public String getRestaurantName() {
                return "Lime Me";
            }

            @Override
            public String getDishName() {
                return "Steak";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        final RestaurantAndMenu menu2 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return UUID.randomUUID().toString();
            }

            @Override
            public String getRestaurantName() {
                return "Orange House";
            }

            @Override
            public String getDishName() {
                return "Spaghetti";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        final RestaurantAndMenu menu3 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return ID;
            }

            @Override
            public String getRestaurantName() {
                return "Lime Me";
            }

            @Override
            public String getDishName() {
                return "Kiwi Juice";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        when(menuRepository.getData(any(), any()))
                .thenReturn(List.of(menu1, menu2, menu3));

        var response = getRestaurantDishesService.execute(REQUEST);
        assertEquals(2, response.getRestaurantList().size());
        assertEquals("Lime Me", response.getRestaurantList().get(0).getRestaurantName());
        assertEquals(2, response.getRestaurantList().get(0).getMenuQuantity());
        assertEquals(2, response.getRestaurantList().get(0).getMenuList().size());
        assertEquals("Kiwi Juice", response.getRestaurantList().get(0).getMenuList().get(0).getName());
        assertEquals("Steak", response.getRestaurantList().get(0).getMenuList().get(1).getName());

        assertEquals("Orange House", response.getRestaurantList().get(1).getRestaurantName());
        assertEquals(1, response.getRestaurantList().get(1).getMenuQuantity());
        assertEquals(1, response.getRestaurantList().get(1).getMenuList().size());

        verify(menuRepository).getData(any(), any());
    }

    @Test
    public void getDishes_TotalDishNotFound_Test() {
        final String ID = UUID.randomUUID().toString();

        final RestaurantAndMenu menu1 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return ID;
            }

            @Override
            public String getRestaurantName() {
                return "Lime Me";
            }

            @Override
            public String getDishName() {
                return "Steak";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        final RestaurantAndMenu menu2 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return UUID.randomUUID().toString();
            }

            @Override
            public String getRestaurantName() {
                return "Orange House";
            }

            @Override
            public String getDishName() {
                return "Spaghetti";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        final RestaurantAndMenu menu3 = new RestaurantAndMenu() {
            @Override
            public String getRestaurantId() {
                return ID;
            }

            @Override
            public String getRestaurantName() {
                return "Lime Me";
            }

            @Override
            public String getDishName() {
                return "Kiwi Juice";
            }

            @Override
            public BigDecimal getPrice() {
                return BigDecimal.TEN;
            }
        };

        when(menuRepository.getData(any(), any()))
                .thenReturn(List.of(menu1, menu2, menu3));

        REQUEST.setTotalDishFrom(3);
        REQUEST.setTotalDishTo(6);

        var response = getRestaurantDishesService.execute(REQUEST);
        assertEquals(0, response.getRestaurantList().size());

        verify(menuRepository).getData(any(), any());
    }
}

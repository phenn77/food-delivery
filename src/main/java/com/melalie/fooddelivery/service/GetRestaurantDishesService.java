package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.RestaurantWithDish;
import com.melalie.fooddelivery.model.entity.Menu;
import com.melalie.fooddelivery.model.request.RestaurantDishesRequest;
import com.melalie.fooddelivery.model.response.RestaurantDishesResponse;
import com.melalie.fooddelivery.repository.MenuRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GetRestaurantDishesService {

    private MenuRepository menuRepository;

    public GetRestaurantDishesService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public RestaurantDishesResponse execute(RestaurantDishesRequest request) {
        var data = processRequest(request);

        return RestaurantDishesResponse.builder()
                .restaurantList(data)
                .build();
    }

    private List<RestaurantWithDish> processRequest(RestaurantDishesRequest request) {
        var data = menuRepository.getData(request.getPriceFrom(), request.getPriceTo());

        Map<String, RestaurantWithDish> container = new HashMap<>();

        data.forEach(x -> {
            var menu = Menu.builder()
                    .name(x.getDishName())
                    .price(x.getPrice())
                    .build();

            var restData = RestaurantWithDish.builder()
                    .restaurantName(x.getRestaurantName())
                    .build();

            if (container.containsKey(x.getRestaurantId())) {
                var currentTotalMenu = container.get(x.getRestaurantId()).getMenuQuantity();
                restData.setMenuQuantity(currentTotalMenu + 1);

                var currentMenu = container.get(x.getRestaurantId()).getMenuList();
                currentMenu.add(menu);

                currentMenu.sort(Comparator.comparing(Menu::getPrice).reversed().thenComparing(Menu::getName));
                restData.setMenuList(currentMenu);
            } else {
                restData.setMenuQuantity(1);

                var m = new ArrayList<Menu>();
                m.add(menu);
                restData.setMenuList(m);
            }

            container.put(x.getRestaurantId(), restData);
        });

        return container
                .values()
                .stream()
                .filter(x -> x.getMenuQuantity() >= request.getTotalDishFrom() && x.getMenuQuantity() <= request.getTotalDishTo())
                .sorted(Comparator.comparing(RestaurantWithDish::getMenuQuantity).reversed()
                        .thenComparing(RestaurantWithDish::getRestaurantName))
                .collect(Collectors.toList());
    }
}

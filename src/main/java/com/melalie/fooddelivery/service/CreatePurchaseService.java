package com.melalie.fooddelivery.service;

import com.melalie.fooddelivery.model.dto.MenuPurchase;
import com.melalie.fooddelivery.model.entity.Menu;
import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.model.entity.UserPurchase;
import com.melalie.fooddelivery.model.request.CreatePurchaseRequest;
import com.melalie.fooddelivery.model.response.CreatePurchaseResponse;
import com.melalie.fooddelivery.repository.MenuRepository;
import com.melalie.fooddelivery.repository.RestaurantRepository;
import com.melalie.fooddelivery.repository.UserPurchaseRepository;
import com.melalie.fooddelivery.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CreatePurchaseService {

    private MenuRepository menuRepository;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    private UserPurchaseRepository userPurchaseRepository;

    public CreatePurchaseService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, UserPurchaseRepository userPurchaseRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.userPurchaseRepository = userPurchaseRepository;
    }

    public CreatePurchaseResponse execute(CreatePurchaseRequest request) throws Exception {
        return generateResponse(request);
    }

    private CreatePurchaseResponse generateResponse(CreatePurchaseRequest request) throws Exception {
        var restaurantId = request.getRestaurantId();

        var user = userRepository.findById(request.getUserId());
        var restaurant = restaurantRepository.findById(restaurantId);
        var menu = menuRepository.findByRestaurant(restaurantId);

        if (user.isEmpty() || restaurant.isEmpty() || menu.isEmpty()) {
            log.error("Data not found. Request: {}", request);

            throw new Exception("Data not found.");
        }

        var currentBalance = restaurant.get().getBalance();
        var userPurchases = processData(request, menu, restaurant.get().getName(), user.get().getId());

        var amount = retrieveBalance(userPurchases, currentBalance, restaurant.get());

        return CreatePurchaseResponse.builder()
                .status("SUCCESS")
                .restaurantName(restaurant.get().getName())
                .previousBalance(currentBalance)
                .currentBalance(amount)
                .purchases(userPurchases)
                .user(user.get().getName())
                .build();
    }

    private Map<String, BigDecimal> retrieveMenuWithPrice(List<Menu> menu) {
        return menu
                .stream()
                .collect(Collectors.toMap(Menu::getId, Menu::getPrice));
    }

    private Map<String, String> retrieveMenuWithName(List<Menu> menu) {
        return menu
                .stream()
                .collect(Collectors.toMap(Menu::getId, Menu::getName));
    }

    private List<MenuPurchase> processData(CreatePurchaseRequest request,
                                           List<Menu> menu,
                                           String restaurantName,
                                           String userId) throws Exception {

        var menuWithPrice = retrieveMenuWithPrice(menu);
        var menuWithName = retrieveMenuWithName(menu);

        List<UserPurchase> userPurchases = new ArrayList<>();
        List<MenuPurchase> menuPurchases = new ArrayList<>();
        request.getPurchases()
                .forEach(purchase -> {
                    if (purchase.getQuantity() != 0) {
                        if (!menuWithPrice.containsKey(purchase.getMenuId()) || !menuWithName.containsKey(purchase.getMenuId())) {
                            log.error("Menu does not exist, will be skipped from being processed. Menu ID: {}", purchase.getMenuId());

                            return;
                        }

                        var menuPurchase = MenuPurchase.builder()
                                .menuId(purchase.getMenuId())
                                .name(menuWithName.get(purchase.getMenuId()))
                                .quantity(purchase.getQuantity())
                                .amount(menuWithPrice.get(purchase.getMenuId()).multiply(BigDecimal.valueOf(purchase.getQuantity())))
                                .build();
                        menuPurchases.add(menuPurchase);

                        for (int i = 0; i < purchase.getQuantity(); i++) {
                            var userPurchase = UserPurchase.builder()
                                    .dish(menuWithName.get(purchase.getMenuId()))
                                    .restaurantName(restaurantName)
                                    .amount(menuWithPrice.get(purchase.getMenuId()))
                                    .date(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())))
                                    .userId(userId)
                                    .build();

                            userPurchases.add(userPurchase);
                        }
                    }
                });

        if (menuPurchases.isEmpty()) {
            log.error("Menu not found. Request: {}", request);

            throw new Exception("Menu not found.");
        }

        userPurchaseRepository.saveAll(userPurchases);

        return menuPurchases;
    }

    private BigDecimal retrieveBalance(List<MenuPurchase> userPurchases, BigDecimal currentBalance, Restaurant restaurant) {
        var getAmount = userPurchases
                .stream()
                .map(MenuPurchase::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var amount = currentBalance.add(getAmount).setScale(2, RoundingMode.DOWN);

        restaurant.setBalance(amount);
        restaurantRepository.save(restaurant);

        return amount;
    }
}

package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.Menu;
import com.melalie.fooddelivery.model.projection.OpenedRestaurant;
import com.melalie.fooddelivery.model.projection.RestaurantAndMenu;
import com.melalie.fooddelivery.model.projection.RestaurantTransactionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM menu " +
                    "WHERE restaurant_id = :restaurantId")
    List<Menu> findByRestaurant(String restaurantId);

    @Query(nativeQuery = true, value =
            "SELECT r.id AS restaurantId, r.name AS restaurantName, m.name AS dishName, m.price " +
                    "FROM menu m " +
                    "JOIN restaurant r  " +
                    "ON m.restaurant_id  = r.id " +
                    "WHERE m.price >= :priceFrom AND m.price <= :priceTo")
    List<RestaurantAndMenu> getData(BigDecimal priceFrom, BigDecimal priceTo);
    
    
    
}

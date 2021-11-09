package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    @Query(nativeQuery = true, value =
            "SELECT r.name as restaurantName " +
                    "FROM restaurant r " +
                    "JOIN menu m " +
                    "ON r.id = m.restaurant_id " +
                    "WHERE m.name = :dishName " +
                    "ORDER BY r.name asc")
    List<String> getRestaurantByDish(String dishName);
}

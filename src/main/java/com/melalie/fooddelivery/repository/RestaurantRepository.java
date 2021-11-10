package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.Restaurant;
import com.melalie.fooddelivery.model.projection.SearchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    @Query(nativeQuery = true, value =
            "SELECT r.name AS restaurantName " +
                    "FROM restaurant r " +
                    "JOIN menu m " +
                    "ON r.id = m.restaurant_id " +
                    "WHERE m.name = :dishName " +
                    "ORDER BY r.name asc")
    List<String> getRestaurantByDish(String dishName);

    @Query(nativeQuery = true, value =
            "SELECT id as restaurantId, name as restaurantName " +
                    "FROM restaurant WHERE LOWER(name) LIKE :keyword ORDER BY name")
    List<SearchData> searchByName(String keyword);

    @Query(nativeQuery = true, value =
            "SELECT m.name AS dishName, r.id AS restaurantId, r.name AS restaurantName " +
                    "FROM menu m " +
                    "JOIN restaurant r " +
                    "ON m.restaurant_id = r.id " +
                    "WHERE LOWER(m.name) like :keyword ORDER BY r.name")
    List<SearchData> searchByDish(String keyword);
}

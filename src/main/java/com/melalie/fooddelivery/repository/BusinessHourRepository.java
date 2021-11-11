package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.BusinessHour;
import com.melalie.fooddelivery.model.projection.OpenedRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, String> {

    @Query(nativeQuery = true, value =
            "SELECT restaurant_id as restaurantId, restaurant_name as restaurantName, COUNT(*) AS total " +
            "FROM restaurant_business_hour " +
            "WHERE hours >= :openHours " +
            "GROUP BY restaurant_id")
    List<OpenedRestaurant> getRestaurantByDay(int openHours);

    @Query(nativeQuery = true, value =
            "SELECT restaurant_id as restaurantId, restaurant_name as restaurantName, SUM(hours) AS total " +
                    "FROM restaurant_business_hour " +
                    "GROUP BY restaurant_id " +
                    "ORDER BY total DESC")
    List<OpenedRestaurant> getRestaurantByWeek(int openHours);
}

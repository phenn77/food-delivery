package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, String> {

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM menu " +
                    "WHERE restaurant_id = :restaurantId")
    List<Menu> findByRestaurant(String restaurantId);
}

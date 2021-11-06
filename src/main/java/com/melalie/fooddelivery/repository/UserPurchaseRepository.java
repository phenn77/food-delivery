package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.UserPurchase;
import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, String> {
    @Query(nativeQuery = true, value =
            "SELECT up.*, up.restaurant_name as restaurantName, u.name " +
            "FROM user u " +
            "JOIN user_purchase up " +
            "ON u.id = up.user_id " +
            "WHERE u.id = :userId")
    List<UserPurchaseData> findByUserId(String userId);
}

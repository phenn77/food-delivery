package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.UserPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, String> {
}

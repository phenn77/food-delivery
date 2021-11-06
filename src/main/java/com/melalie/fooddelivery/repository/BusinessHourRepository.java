package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.BusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, String> {
}

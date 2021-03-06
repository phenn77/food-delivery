package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(nativeQuery = true, value =
            "SELECT * " +
            "FROM USER " +
            "WHERE LOWER(name) LIKE LOWER(:name)")
    Optional<User> findByName(String name);
}

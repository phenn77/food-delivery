package com.melalie.fooddelivery.repository;

import com.melalie.fooddelivery.model.entity.UserPurchase;
import com.melalie.fooddelivery.model.projection.RestaurantTransactionData;
import com.melalie.fooddelivery.model.projection.UserPurchaseData;
import com.melalie.fooddelivery.model.projection.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, String> {
    @Query(nativeQuery = true, value =
            "SELECT up.*, up.restaurant_name as restaurantName, u.name, u.id as userId " +
                    "FROM user u " +
                    "JOIN user_purchase up " +
                    "ON u.id = up.user_id " +
                    "WHERE u.id = :userId")
    List<UserPurchaseData> findByUserId(String userId);

    @Query(nativeQuery = true, value =
            "SELECT up.*, up.restaurant_name as restaurantName, u.name, u.id as userId " +
                    "FROM user u " +
                    "JOIN user_purchase up " +
                    "ON u.id = up.user_id " +
                    "WHERE lower(u.name) like lower(:name)")
    List<UserPurchaseData> findByUserName(String name);

    @Query(nativeQuery = true, value =
            "SELECT up.*, up.restaurant_name as restaurantName, m.restaurant_id AS restaurantId " +
                    "FROM user_purchase up " +
                    "JOIN menu m ON up.dish = m.name and up.amount = m.price " +
                    "WHERE m.restaurant_id = :restaurantId")
    List<RestaurantTransactionData> retrieveRestaurantTransactions(String restaurantId);

    @Query(nativeQuery = true, value =
            "SELECT up.*, up.restaurant_name as restaurantName, m.restaurant_id AS restaurantId " +
                    "FROM user_purchase up " +
                    "JOIN menu m ON up.dish = m.name and up.amount = m.price " +
                    "WHERE up.restaurant_name  = :restaurantName")
    List<RestaurantTransactionData> retrieveRestaurantTransactionsByName(String restaurantName);

    @Query(nativeQuery = true, value =
            "SELECT  m.restaurant_id as restaurantId, up.restaurant_name as restaurantName, up.amount as amount, up.dish " +
                    "FROM user_purchase up " +
                    "JOIN menu m ON up.dish = m.name and up.amount = m.price")
    List<RestaurantTransactionData> retrieveTransactions();


    @Query(nativeQuery = true, value =
            "SELECT COUNT(*) " +
                    "FROM " +
                    "(SELECT user_id, SUM(amount) AS total " +
                    "FROM user_purchase " +
                    "WHERE date BETWEEN :fromDate AND :toDate " +
                    "GROUP BY user_id) PURCHASE " +
                    "WHERE total <= :amount")
    Integer getMinTransaction(String fromDate, String toDate, BigDecimal amount);

    @Query(nativeQuery = true, value =
            "SELECT COUNT(*) " +
                    "FROM " +
                    "(SELECT user_id, SUM(amount) AS total " +
                    "FROM user_purchase " +
                    "WHERE date BETWEEN :fromDate AND :toDate " +
                    "GROUP BY user_id) PURCHASE " +
                    "WHERE total >= :amount")
    Integer getMaxTransaction(String fromDate, String toDate, BigDecimal amount);

    @Query(nativeQuery = true, value =
            "SELECT up.user_id, u.name, COUNT(up.*) as totalTransaction, SUM(up.amount) as totalAmount " +
                    "FROM USER_PURCHASE up " +
                    "join user u " +
                    "on up.user_id = u.id " +
                    "WHERE up.date BETWEEN :fromDate AND :toDate " +
                    "GROUP BY up.user_id " +
                    "ORDER BY totalTransaction DESC, u.name LIMIT :totalUser")
    List<UserTransaction> getUsersTransaction(String fromDate, String toDate, Integer totalUser);
}

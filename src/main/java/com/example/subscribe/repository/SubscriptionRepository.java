package com.example.subscribe.repository;

import com.example.subscribe.entity.Subscription;
import com.example.subscribe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("""
        SELECT SUM(s.amount) 
        FROM Subscription s 
        WHERE s.user.id = :userId 
          AND MONTH(s.paymentDay) = :month 
          AND YEAR(s.paymentDay) = :year
    """)
    Integer getTotalAmountByMonth(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT s.category, SUM(s.amount) 
        FROM Subscription s 
        WHERE s.user.id = :userId 
          AND MONTH(s.paymentDay) = :month 
          AND YEAR(s.paymentDay) = :year 
        GROUP BY s.category
    """)
    List<Object[]> getAmountGroupedByCategory(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId")
    List<Subscription> findByUser_UserId(@Param("userId") Long userId);

}

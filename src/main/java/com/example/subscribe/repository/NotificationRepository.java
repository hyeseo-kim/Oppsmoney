package com.example.subscribe.repository;

import com.example.subscribe.entity.Notification;
import com.example.subscribe.entity.Subscription;
import com.example.subscribe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.notification_date DESC")
    List<Notification> findByUserId(@Param("user") User user);

    @Query("""
        SELECT COUNT(n) > 0 
        FROM Notification n 
        WHERE n.user = :user 
          AND n.subscription = :subscription 
          AND n.type = :type 
          AND n.notification_date BETWEEN :start AND :end
    """)
    boolean existsNotificationToday(
            @Param("user") User user,
            @Param("subscription") Subscription subscription,
            @Param("type") String type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);

}

package com.example.subscribe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "service_name")
    private String service_name;

    private String category;

    private int amount;

    @Column(name = "payment_day")
    private LocalDate paymentDay;

    @Column(name = "alarm_days_before")
    private int alarm_days_before;

    @Column(name = "next_month_use")
    private Boolean next_month_use;

    private String memo;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "alarm_enabled")
    private boolean alarmEnabled = true;
}

package com.example.subscribe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionStatsDTO {
    private String category;
    private int totalAmount;
}

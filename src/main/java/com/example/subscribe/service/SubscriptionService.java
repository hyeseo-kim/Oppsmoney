package com.example.subscribe.service;

import com.example.subscribe.dto.SubscriptionStatsDTO;
import com.example.subscribe.entity.Subscription;
import com.example.subscribe.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    // ✅ 카테고리별 통계 (DTO 리스트)
    public List<SubscriptionStatsDTO> getMonthlyCategoryStats(Long userId) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        List<Object[]> raw = subscriptionRepository.getAmountGroupedByCategory(userId, month, year);
        return raw.stream()
                .map(obj -> new SubscriptionStatsDTO(
                        (String) obj[0],
                        ((Number) obj[1]).intValue()
                ))
                .toList();
    }

    public Map<String, Integer> getMonthlyCategoryTotals(Long userId) {
        List<Subscription> subs = subscriptionRepository.findByUser_UserId(userId);

        YearMonth currentMonth = YearMonth.now(); // 예: 2025-07
        YearMonth lastMonth = currentMonth.minusMonths(1);

        Map<String, Integer> categoryMap = new HashMap<>();

        for (Subscription sub : subs) {
            LocalDate paymentDate = sub.getPaymentDay();
            if (paymentDate == null) continue;

            YearMonth subMonth = YearMonth.from(paymentDate);
            boolean include = false;

            // ✅ 이번 달이면 무조건 포함
            if (subMonth.equals(currentMonth)) {
                include = true;
            }

            // ✅ 저번 달이고 next_month_use가 true이면 포함
            else if (subMonth.equals(lastMonth) && Boolean.TRUE.equals(sub.getNext_month_use())) {
                include = true;
            }

            if (include) {
                String category = sub.getCategory();
                int amount = sub.getAmount();
                categoryMap.put(category, categoryMap.getOrDefault(category, 0) + amount);
            }
        }

        return categoryMap;
    }

    public int getMonthlyTotal(Long userId) {
        List<Subscription> subs = subscriptionRepository.findByUser_UserId(userId);

        YearMonth currentMonth = YearMonth.now();         // 예: 2025-07
        YearMonth lastMonth = currentMonth.minusMonths(1); // 예: 2025-06

        return subs.stream()
                .filter(sub -> {
                    LocalDate paymentDate = sub.getPaymentDay();
                    if (paymentDate == null) return false;

                    YearMonth subMonth = YearMonth.from(paymentDate);

                    // ✅ 이번 달이면 무조건 포함
                    if (subMonth.equals(currentMonth)) return true;

                    // ✅ 저번 달이면서 next_month_use가 true인 것도 포함
                    return subMonth.equals(lastMonth) && Boolean.TRUE.equals(sub.getNext_month_use());
                })
                .mapToInt(Subscription::getAmount)
                .sum();
    }

    public Map<String, Integer> getMonthlyComparison(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);

        Integer thisMonth = subscriptionRepository.getTotalAmountByMonth(
                userId, now.getMonthValue(), now.getYear()
        );
        Integer previousMonth = subscriptionRepository.getTotalAmountByMonth(
                userId, lastMonth.getMonthValue(), lastMonth.getYear()
        );

        Map<String, Integer> result = new HashMap<>();
        result.put("이번 달", thisMonth != null ? thisMonth : 0);
        result.put("지난 달", previousMonth != null ? previousMonth : 0);

        return result; 
    }

}

package com.example.subscribe.controller;

import com.example.subscribe.entity.Subscription;
import com.example.subscribe.repository.NotificationRepository;
import com.example.subscribe.repository.SubscriptionRepository;
import com.example.subscribe.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * ✅ 대시보드 페이지 컨트롤러
 * 로그인한 사용자의 구독 정보, 카테고리별 합계, 미읽은 알림 수, 월 비교 그래프 등을 조회
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final NotificationRepository notificationRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String userNickname = (String) session.getAttribute("userNickname");

        if (userId == null) {
            return "redirect:/users/login"; // 로그인 안 했을 경우
        }

        // ✅ 1. 사용자 구독 목록
        List<Subscription> subscriptions = subscriptionRepository.findByUser_UserId(userId);

        // ✅ 2. 이번 달 총 구독 금액
        int totalAmount = subscriptionService.getMonthlyTotal(userId);

        // ✅ 3. 카테고리별 금액 합계
        Map<String, Integer> categoryTotals = subscriptionService.getMonthlyCategoryTotals(userId);

        // ✅ 4. 읽지 않은 알림 수
        Long unreadCount = notificationRepository.countUnreadByUserId(userId);

        // ✅ 5. 지난달 vs 이번달 금액 비교
        Map<String, Integer> monthlyComparison = subscriptionService.getMonthlyComparison(userId);

        // ✅ 모델 전달
        model.addAttribute("userNickname", userNickname);
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("monthlyComparison", monthlyComparison);

        return "dashboard"; // templates/dashboard.html
    }
}

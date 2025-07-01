package com.example.subscribe.service;

import com.example.subscribe.entity.Notification;
import com.example.subscribe.entity.Subscription;
import com.example.subscribe.repository.NotificationRepository;
import com.example.subscribe.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService; // ✅ 이메일 서비스 주입

    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시
    @Transactional
    public void sendNotifications() {
        System.out.println("=== [SCHEDULER] 알림 스케줄러 실행됨 ===");

        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for (Subscription sub : subscriptions) {
            if (!sub.isAlarmEnabled()) continue; // 🔥 알림 OFF인 구독은 건너뜀

            LocalDate targetDate = sub.getPaymentDay().minusDays(sub.getAlarm_days_before());

            if (today.equals(targetDate)) {
                boolean alreadyExists = notificationRepository.existsNotificationToday(
                        sub.getUser(), sub, "결제 전 알림", startOfToday, endOfToday
                );

                if (!alreadyExists) {
                    Notification noti = Notification.builder()
                            .user(sub.getUser())
                            .subscription(sub)
                            .notification_date(LocalDateTime.now())
                            .type("결제 전 알림")
                            .isRead(false)
                            .build();

                    notificationRepository.save(noti);

                    // ✅ 이메일 전송
                    String to = sub.getUser().getEmail();
                    String subject = "[구독 알림] " + sub.getService_name() + " 결제 예정 알림";
                    String content = "안녕하세요. " + sub.getUser().getNickname() + "님!\n\n" +
                            sub.getService_name() + " 구독 결제일이 " + sub.getPaymentDay() +
                            "로 예정되어 있어 미리 알려드립니다.\n\n감사합니다.";
                    emailService.sendEmail(to, subject, content);

                    System.out.println("[알림] " + to + "님에게 이메일 전송 완료!");
                } else {
                    System.out.println("🔁 이미 오늘 알림 전송됨: " + sub.getUser().getEmail());
                }
            }
        }
    }
}

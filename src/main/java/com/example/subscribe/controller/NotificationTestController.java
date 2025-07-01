package com.example.subscribe.controller;

import com.example.subscribe.entity.Notification;
import com.example.subscribe.entity.Subscription;
import com.example.subscribe.entity.User;
import com.example.subscribe.repository.NotificationRepository;
import com.example.subscribe.repository.SubscriptionRepository;
import com.example.subscribe.repository.UserRepository;
import com.example.subscribe.service.EmailService;
import com.example.subscribe.service.NotificationScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * ✅ 알림 스케줄러 테스트용 컨트롤러
 * 운영 환경에서는 반드시 비공개 처리하거나 관리자 인증 필요
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationTestController {


    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final NotificationScheduler notificationScheduler;

    /**
     * 📌 테스트용 수동 알림 전송 엔드포인트
     * 호출 시 이번 달 알림일이 도래한 구독 항목에 대해 사용자에게 알림 전송
     *
     * @return 상태 메시지
     */
    @GetMapping("/admin/test/send-notifications")
    public String testSendNotifications() {
        log.info("🔔 [관리자] 수동 알림 전송 테스트 시작");
        notificationScheduler.sendNotifications();
        log.info("✅ 알림 전송 완료");

        return "✅ 테스트용 알림 전송이 완료되었습니다.";
    }

    @GetMapping("/admin/test/send-one")
    public String sendOneTestNotification() {
        log.info("🔔 [관리자] 단일 알림 테스트 시작");

        Long userId = 2L; // 테스트할 사용자 ID
        Long subscriptionId = 1L; // 테스트할 구독 ID

        // 🔹 주입 필요
        User user = userRepository.findById(userId).orElse(null);
        Subscription sub = subscriptionRepository.findById(subscriptionId).orElse(null);

        if (user == null || sub == null) {
            return "❌ 사용자 또는 구독 항목을 찾을 수 없습니다.";
        }

        Notification noti = Notification.builder()
                .user(user)
                .subscription(sub)
                .notification_date(LocalDateTime.now())
                .type("테스트 알림")
                .isRead(false)
                .build();

        notificationRepository.save(noti);

        // 이메일도 함께 테스트 전송
        emailService.sendEmail(
                user.getEmail(),
                "[테스트 알림] " + sub.getService_name() + " 결제 알림",
                "안녕하세요, " + user.getNickname() + "님!\n" +
                        "이것은 테스트용으로 보낸 " + sub.getService_name() + " 결제 예정 알림입니다."
        );

        log.info("✅ 테스트 알림 전송 완료");

        return "✅ 단일 테스트 알림 전송 완료!";
    }

}

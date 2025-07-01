//package com.example.subscribe.controller;
//
//import com.example.subscribe.service.NotificationScheduler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class TestController {
//
//    private final NotificationScheduler scheduler;
//
//    // 수동 알림 전송 테스트용
//    @GetMapping("/notifications/test")
//    public String runNotificationManually() {
//        scheduler.sendNotifications();
//        return "✅ 수동 알림 트리거 완료!";
//    }
//}

//package com.example.subscribe.controller;
//
//import com.example.subscribe.entity.Notification;
//import com.example.subscribe.repository.NotificationRepository;
//import com.example.subscribe.service.NotificationService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
////@RestController
//@RequiredArgsConstructor
//@RequestMapping("/notifications")
//public class NotificationController {
//
//    private final NotificationRepository notificationRepository;
//    private final NotificationService notificationService;
//
//    // 🔔 전체 알림 조회
//    @GetMapping("/my")
//    public List<Notification> getMyNotifications(HttpSession session){
//        Long userId = (Long) session.getAttribute("userId");
//        if(userId == null){
//            return List.of();
//        }
//        return notificationRepository.findByUserId(userId);
//    }
//
//    // ✅ 알림 읽음 처리
//    @PostMapping("/{id}/read")
//    public String markAsRead(@PathVariable Long id){
//        return notificationService.markAsRead(id);
//    }
//
//    // ✅ 읽지 않은 알림만 조회
//    @GetMapping("/unread")
//    public List<Notification> getUnreadNotifications(HttpSession session){
//        Long userId = (Long) session.getAttribute("userId");
//        if(userId == null){
//            return List.of();
//        }
//        // ✅ 수정된 부분: 읽지 않은 알림만 가져오도록 변경
//        return notificationRepository.findUnreadByUserId(userId);
//    }
//
//    //알림 전체 읽기
//    @PostMapping("/read-many")
//    public String markAsReadMany(@RequestBody List<Long> ids){
//        return notificationService.markAsReadMany(ids);
//    }
//
//    // 알림 1개 삭제
//    @DeleteMapping("/{id}/delete")
//    public String deleteNotification(@PathVariable Long id) {
//        return notificationService.deleteNotification(id);
//    }
//
//    // 알림 여러 개 삭제
//    @PostMapping("/delete-many")
//    public String deleteManyNotifications(@RequestBody List<Long> ids) {
//        return notificationService.deleteManyNotifications(ids);
//    }
//
//}

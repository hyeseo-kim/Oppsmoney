package com.example.subscribe.controller;

import com.example.subscribe.entity.Notification;
import com.example.subscribe.entity.User;
import com.example.subscribe.repository.UserRepository;
import com.example.subscribe.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationWebController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    /** ✅ 알림 목록 조회 */
    @GetMapping
    public String showNotifications(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "redirect:/users/login";

        List<Notification> notifications = notificationService.findByUser(user);
        model.addAttribute("notifications", notifications);
        return "notification-list";
    }

    /** ✅ 여러 알림 읽음 처리 */
    @PostMapping("/read-many")
    public String markAsReadMany(@RequestParam(value = "ids", required = false) List<Long> ids,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "알림을 선택해주세요.");
            return "redirect:/notifications";
        }

        notificationService.markAsReadMany(ids);
        redirectAttributes.addFlashAttribute("message", "선택한 알림을 읽음 처리했습니다.");
        return "redirect:/notifications";
    }

    /** ✅ 단일 알림 삭제 */
    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        String result = notificationService.deleteNotificationByUser(id, userId);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/notifications";
    }

    /** ✅ 여러 알림 삭제 */
    @PostMapping("/delete-many")
    public String deleteManyNotifications(@RequestParam("ids") List<Long> ids,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        String result = notificationService.deleteManyNotificationsByUser(ids, userId);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/notifications";
    }
}

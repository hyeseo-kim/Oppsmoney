package com.example.subscribe.service;

import com.example.subscribe.entity.Notification;
import com.example.subscribe.entity.User;
import com.example.subscribe.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public String markAsRead(Long notificationId) {
        Optional<Notification> opt = notificationRepository.findById(notificationId);
        if (opt.isEmpty()) return "해당 알림을 찾을 수 없습니다.";

        Notification notification = opt.get();
        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
            return "✅알람 읽음 처리 완료!";
        } else {
            return "💡이미 읽은 알림입니다.";
        }
    }

    @Transactional
    public String markAsReadMany(List<Long> ids) {
        List<Notification> notifications = notificationRepository.findAllById(ids);
        for (Notification n : notifications) {
            n.setRead(true);
        }
        notificationRepository.saveAll(notifications);
        return "✅ 선택한 알림을 읽음 처리했습니다.";
    }

    @Transactional
    public String deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            return "❌ 해당 알림이 존재하지 않습니다.";
        }
        notificationRepository.deleteById(id);
        return "🗑️ 알림이 삭제되었습니다.";
    }

    @Transactional
    public String deleteManyNotifications(List<Long> ids) {
        List<Notification> toDelete = notificationRepository.findAllById(ids);
        if (toDelete.isEmpty()) {
            return "❌ 삭제할 알림이 없습니다.";
        }
        notificationRepository.deleteAll(toDelete);
        return "🗑️ 선택한 알림이 삭제되었습니다.";
    }

    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }


    @Transactional
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUserId(user);
    }

    @Transactional
    public String deleteNotificationByUser(Long id, Long userId) {
        return notificationRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(userId))
                .map(n -> {
                    notificationRepository.delete(n);
                    return "🗑️ 알림이 삭제되었습니다.";
                })
                .orElse("❌ 권한이 없거나 알림이 존재하지 않습니다.");
    }

    @Transactional
    public String deleteManyNotificationsByUser(List<Long> ids, Long userId) {
        List<Notification> deletable = notificationRepository.findAllById(ids).stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .toList();

        if (deletable.isEmpty()) return "❌ 삭제할 수 있는 알림이 없습니다.";

        notificationRepository.deleteAll(deletable);
        return "🗑️ 선택한 알림이 삭제되었습니다.";
    }


}

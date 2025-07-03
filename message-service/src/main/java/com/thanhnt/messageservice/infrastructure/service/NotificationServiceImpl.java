package com.thanhnt.messageservice.infrastructure.service;

import com.thanhnt.messageservice.application.service.NotificationService;
import com.thanhnt.messageservice.domain.entity.notifications.Notification;
import com.thanhnt.messageservice.domain.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;

  @Override
  @Transactional
  public void saveNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  @Override
  public Page<Notification> getNotifications(int page, int size, Long userId) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Notification> notifications =
        notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

    List<Notification> unread =
        notifications.getContent().stream()
            .filter(n -> !n.isRead())
            .peek(Notification::isNotificationRead)
            .toList();

    if (!unread.isEmpty()) {
      notificationRepository.saveAll(unread);
    }

    return notifications;
  }
}

package com.thanhnt.messageservice.application.service;

import com.thanhnt.messageservice.domain.entity.notifications.Notification;
import org.springframework.data.domain.Page;

public interface NotificationService {
  void saveNotification(Notification notification);

  Page<Notification> getNotifications(int page, int size, Long userId);
}

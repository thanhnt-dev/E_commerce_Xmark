package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.NotificationDTO;

public interface NotificationProducer {
  void sendNotification(NotificationDTO notificationDTO);
}

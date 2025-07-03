package com.thanhnt.storeservice.application.service;

import com.thanhnt.storeservice.application.dto.StoreRegistrationNotificationDTO;

public interface StoreMailQueueProducer {
  void sendStoreRegistrationNotification(
      StoreRegistrationNotificationDTO storeRegistrationNotificationDTO);
}

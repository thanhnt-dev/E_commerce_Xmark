package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.dto.NotificationDTO;
import com.thanhnt.productservice.application.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerImpl implements NotificationProducer {
  private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

  @Value("${spring.kafka.topics.notification}")
  private String notificationTopic;

  @Override
  public void sendNotification(NotificationDTO notificationDTO) {
    log.info("Sending notification: " + notificationDTO.toString());
    kafkaTemplate.send(notificationTopic, notificationDTO);
  }
}

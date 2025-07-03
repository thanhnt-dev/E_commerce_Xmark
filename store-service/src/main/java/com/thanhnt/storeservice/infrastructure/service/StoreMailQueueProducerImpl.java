package com.thanhnt.storeservice.infrastructure.service;

import com.thanhnt.storeservice.application.dto.StoreRegistrationNotificationDTO;
import com.thanhnt.storeservice.application.service.StoreMailQueueProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreMailQueueProducerImpl implements StoreMailQueueProducer {
  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmq.exchangeName}")
  private String exchange;

  @Value("${rabbitmq.store-mail-routing-key}")
  private String userMailQueueRoutingKey;

  private static final Logger LOGGER = LoggerFactory.getLogger(StoreMailQueueProducerImpl.class);

  @Override
  public void sendStoreRegistrationNotification(
      StoreRegistrationNotificationDTO storeRegistrationNotificationDTO) {
    LOGGER.info(
        "Sending mail message to queue: " + storeRegistrationNotificationDTO.getReceiverMail());
    rabbitTemplate.convertAndSend(
        exchange, userMailQueueRoutingKey, storeRegistrationNotificationDTO);
  }
}

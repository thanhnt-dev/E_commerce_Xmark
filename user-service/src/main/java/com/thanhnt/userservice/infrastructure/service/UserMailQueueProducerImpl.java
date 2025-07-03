package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.application.dto.OTPMailDTO;
import com.thanhnt.userservice.application.service.UserMailQueueProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMailQueueProducerImpl implements UserMailQueueProducer {
  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmq.exchangeName}")
  private String exchange;

  @Value("${rabbitmq.user-mail-routing-key}")
  private String userMailQueueRoutingKey;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserMailQueueProducerImpl.class);

  @Override
  public void sendMailMessage(OTPMailDTO mailDTO) {
    LOGGER.info("Sending mail message to queue: " + mailDTO.getReceiverMail());
    rabbitTemplate.convertAndSend(exchange, userMailQueueRoutingKey, mailDTO);
  }
}

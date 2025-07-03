package com.thanhnt.mailservice.service.impl;

import com.thanhnt.mailservice.global.message.OTPMailMessage;
import com.thanhnt.mailservice.global.message.StoreRegistrationMessage;
import com.thanhnt.mailservice.service.RabbitMQConsumer;
import com.thanhnt.mailservice.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RabbitMQConsumerImpl implements RabbitMQConsumer {
  private final SendMailService sendMailService;

  @Override
  @RabbitListener(queues = {"${rabbitmq.user-mail-queue}"})
  @Retryable(
      value = {MailException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 8000))
  public void consumeOTPMailMessage(OTPMailMessage otpMailMessage) {
    log.info("Consuming mail: " + otpMailMessage.getReceiverMail());
    try {
      sendMailService.sendOTPMail(otpMailMessage);
    } catch (Exception e) {
      log.info("Failed to send mail: " + otpMailMessage.getReceiverMail(), e);
    }
  }

  @Override
  @RabbitListener(queues = {"${rabbitmq.store-mail-routing-key}"})
  @Retryable(
      value = {MailException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 8000))
  public void consumeStoreRegistrationMail(StoreRegistrationMessage storeRegistrationMessage) {
    log.info("Consuming store registration mail: " + storeRegistrationMessage.getReceiverMail());
    try {
      sendMailService.sendStoreRegistrationMail(storeRegistrationMessage);
    } catch (Exception e) {
      log.error(
          "Failed to send store registration mail: " + storeRegistrationMessage.getReceiverMail(),
          e);
    }
  }
}

package com.thanhnt.mailservice.service;

import com.thanhnt.mailservice.global.message.OTPMailMessage;
import com.thanhnt.mailservice.global.message.StoreRegistrationMessage;

public interface RabbitMQConsumer {
  void consumeOTPMailMessage(OTPMailMessage otpMailMessage);

  void consumeStoreRegistrationMail(StoreRegistrationMessage storeRegistrationMessage);
}

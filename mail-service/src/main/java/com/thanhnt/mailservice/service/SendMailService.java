package com.thanhnt.mailservice.service;

import com.thanhnt.mailservice.global.message.OTPMailMessage;
import com.thanhnt.mailservice.global.message.StoreRegistrationMessage;

public interface SendMailService {
  void sendOTPMail(OTPMailMessage message);

  void sendStoreRegistrationMail(StoreRegistrationMessage message);
}

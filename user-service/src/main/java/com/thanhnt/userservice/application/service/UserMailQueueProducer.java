package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.application.dto.OTPMailDTO;

public interface UserMailQueueProducer {
  void sendMailMessage(OTPMailDTO mailDTO);
}

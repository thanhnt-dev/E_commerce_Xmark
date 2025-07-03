package com.thanhnt.mailservice.global.message;

import com.thanhnt.mailservice.global.enums.OTPType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OTPMailMessage {
  private String receiverMail;
  private String otpCode;
  private OTPType type;
}

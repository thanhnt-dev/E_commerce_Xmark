package com.thanhnt.userservice.application.dto;

import com.thanhnt.userservice.domain.entity.commons.OTPType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPMailDTO {
  private String receiverMail;
  private String otpCode;
  private OTPType type;
}

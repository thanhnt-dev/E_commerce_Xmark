package com.thanhnt.mailservice.global.message;

import com.thanhnt.mailservice.global.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegistrationMessage {
  private String receiverMail;
  private String ownerName;
  private String reason;
  private VerificationStatus verificationStatus;
}

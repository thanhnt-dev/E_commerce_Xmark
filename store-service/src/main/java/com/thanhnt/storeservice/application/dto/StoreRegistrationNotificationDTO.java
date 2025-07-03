package com.thanhnt.storeservice.application.dto;

import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegistrationNotificationDTO {
  private String receiverMail;
  private String ownerName;
  private String reason;
  private VerificationStatus verificationStatus;
}

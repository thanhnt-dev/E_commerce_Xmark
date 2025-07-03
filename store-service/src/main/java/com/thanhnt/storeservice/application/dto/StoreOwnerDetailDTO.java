package com.thanhnt.storeservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StoreOwnerDetailDTO {
  private Long storeOwnerId;
  private String storeOwnerName;
  private String storeOwnerEmail;
  private String storeOwnerPhone;
  private String storeOwnerAvatar;
  private Long dateOfBirth;
  private Long createdAt;
}

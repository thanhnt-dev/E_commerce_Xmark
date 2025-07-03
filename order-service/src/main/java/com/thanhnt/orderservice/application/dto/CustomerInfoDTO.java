package com.thanhnt.orderservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoDTO {
  private Long id;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String address;
}

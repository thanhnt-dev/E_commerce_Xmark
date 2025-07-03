package com.thanhnt.storeservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StoreRequestResponse {
  private Long id;
  private String adminEmail;
  private String reason;
  private Long reviewAt;
}

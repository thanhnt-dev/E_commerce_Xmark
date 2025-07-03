package com.thanhnt.orderservice.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMomoResponse {
  private String partnerCode;
  private String orderId;
  private String requestId;
  private Long amount;
  private Long responseTime;
  private String message;
  private int resultCode;
  private String payUrl;
  private String deepLink;
  private String qrCodeUrl;
}

package com.thanhnt.orderservice.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CreateMomoRequest {

  private String partnerCode;
  private String requestType;
  private String ipnUrl;

  @NotNull(message = "Order code cannot be null")
  private String orderId;

  private Long amount;
  private String orderInfo;
  private String requestId;
  private String lang;
  private String extraData;
  private String redirectUrl;
  private String signature;
}

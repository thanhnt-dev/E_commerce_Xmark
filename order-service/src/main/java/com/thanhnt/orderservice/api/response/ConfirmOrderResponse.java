package com.thanhnt.orderservice.api.response;

import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConfirmOrderResponse {
  private String orderCode;
  private PaymentType paymentType;
}

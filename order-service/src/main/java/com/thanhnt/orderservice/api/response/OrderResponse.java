package com.thanhnt.orderservice.api.response;

import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
  private Long id;
  private String orderCode;
  private String customerName;
  private OrderStatus status;
  private PaymentType paymentType;
  private BigDecimal price;
  private Long createdAt;
}

package com.thanhnt.orderservice.api.response;

import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderCustomerResponse {
  private Long id;
  private String orderCode;
  private String shopName;
  private Long shopId;
  private BigDecimal price;
  private PaymentType paymentType;
  private OrderStatus status;
  private List<OrderItemResponse> orderItems;
}

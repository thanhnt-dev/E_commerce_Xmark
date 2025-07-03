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
@Getter
@Builder
public class OrderDetailResponse {
  private Long shopId;
  private String orderCode;
  private String shopName;
  private String shippingAddress;
  private Long voucherId;
  private String voucherCode;
  private String note;
  private String customerName;
  private String customerPhone;
  private List<OrderItemResponse> orderItems;
  private BigDecimal totalPrice;
  private BigDecimal totalDiscount;
  private BigDecimal totalShippingFee;
  private BigDecimal totalAmount;
  private OrderStatus orderStatus;
  private PaymentType paymentType;
  private Long createdAt;
  private boolean valid;
}

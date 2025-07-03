package com.thanhnt.orderservice.domain.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
  PAYMENT_PENDING("PAYMENT_PENDING", "Đang chờ thanh toán"),
  PAYMENT_SUCCESS("PAYMENT_SUCCESS", "Thanh toán thành công"),
  PAYMENT_FAILED("PAYMENT_FAILED", "Thanh toán thất bại"),
  PENDING("PENDING", "Đang chờ xử lý"),
  SHOP_CONFIRMED("SHOP_CONFIRMED", "Đã xác nhận"),
  PROCESSING("PROCESSING", "Đang xử lý"),
  COMPLETED("COMPLETED", "Đã hoàn thành"),
  CANCELLED("CANCELLED", "Đã hủy"),
  REFUNDED("REFUNDED", "Đã hoàn tiền");
  private final String code;
  private final String description;

  public boolean isPending() {
    return this == PENDING;
  }

  public boolean isProcessing() {
    return this == PROCESSING;
  }

  public boolean isCompleted() {
    return this == COMPLETED;
  }

  public boolean isCancelled() {
    return this == CANCELLED;
  }

  public boolean isRefunded() {
    return this == REFUNDED;
  }
}

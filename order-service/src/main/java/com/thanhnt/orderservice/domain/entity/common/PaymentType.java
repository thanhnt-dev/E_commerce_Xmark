package com.thanhnt.orderservice.domain.entity.common;

import lombok.Getter;

@Getter
public enum PaymentType {
  CASH_ON_DELIVERY("CASH_ON_DELIVERY", "Thanh toán khi nhận hàng"),
  ONLINE_PAYMENT("ONLINE_PAYMENT", "Thanh toán trực tuyến");

  private final String code;
  private final String description;

  PaymentType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public boolean isOnlinePayment() {
    return this == ONLINE_PAYMENT;
  }

  public boolean isCashOnDelivery() {
    return this == CASH_ON_DELIVERY;
  }
}

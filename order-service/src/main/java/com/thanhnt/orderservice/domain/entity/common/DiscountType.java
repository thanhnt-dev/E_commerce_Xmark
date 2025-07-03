package com.thanhnt.orderservice.domain.entity.common;

public enum DiscountType {
  PERCENTAGE,
  AMOUNT;

  public boolean isPercentage() {
    return this == PERCENTAGE;
  }

  public boolean isAmount() {
    return this == AMOUNT;
  }
}

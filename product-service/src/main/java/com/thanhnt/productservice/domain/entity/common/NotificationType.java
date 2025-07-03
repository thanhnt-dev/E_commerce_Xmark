package com.thanhnt.productservice.domain.entity.common;

public enum NotificationType {
  SHOP,
  PRODUCT;

  public boolean isShop() {
    return this == SHOP;
  }

  public boolean isProduct() {
    return this == PRODUCT;
  }
}

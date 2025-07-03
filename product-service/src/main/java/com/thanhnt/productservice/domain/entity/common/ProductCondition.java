package com.thanhnt.productservice.domain.entity.common;

public enum ProductCondition {
  NEW("Mới"),
  LIKE_NEW("Như mới"),
  USED("Cũ");

  private final String value;

  ProductCondition(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

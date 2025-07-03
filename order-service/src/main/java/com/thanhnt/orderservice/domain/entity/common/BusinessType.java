package com.thanhnt.orderservice.domain.entity.common;

public enum BusinessType {
  INDIVIDUAL,
  BUSINESS;

  public boolean isIndividual() {
    return this == INDIVIDUAL;
  }

  public boolean isBusinessType() {
    return this == BUSINESS;
  }
}

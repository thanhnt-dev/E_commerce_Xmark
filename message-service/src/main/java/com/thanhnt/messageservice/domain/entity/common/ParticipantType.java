package com.thanhnt.messageservice.domain.entity.common;

public enum ParticipantType {
  USER,
  SHOP;

  public boolean isUser() {
    return this == USER;
  }

  public boolean isShop() {
    return this == SHOP;
  }
}

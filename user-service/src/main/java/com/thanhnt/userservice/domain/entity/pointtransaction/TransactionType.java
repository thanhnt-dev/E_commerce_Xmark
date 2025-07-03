package com.thanhnt.userservice.domain.entity.pointtransaction;

public enum TransactionType {
  EARN,
  SPEND;

  public boolean isEarn() {
    return this == EARN;
  }

  public boolean isSpend() {
    return this == SPEND;
  }
}

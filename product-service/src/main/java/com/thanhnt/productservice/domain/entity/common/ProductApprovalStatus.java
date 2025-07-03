package com.thanhnt.productservice.domain.entity.common;

public enum ProductApprovalStatus {
  PENDING,
  APPROVED,
  REJECTED;

  public boolean isPending() {
    return this == PENDING;
  }

  public boolean isApproved() {
    return this == APPROVED;
  }

  public boolean isRejected() {
    return this == REJECTED;
  }
}

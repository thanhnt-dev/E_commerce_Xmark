package com.thanhnt.storeservice.domain.entity.common;

public enum SubscriptionStatus {
  ACTIVE,
  EXPIRED,
  CANCELLED,
  SUSPENDED,
  PENDING;

  public boolean isActive() {
    return this == ACTIVE;
  }

  public boolean isExpired() {
    return this == EXPIRED;
  }

  public boolean isCancelled() {
    return this == CANCELLED;
  }

  public boolean isSuspended() {
    return this == SUSPENDED;
  }

  public boolean isPending() {
    return this == PENDING;
  }
}

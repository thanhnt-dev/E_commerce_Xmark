package com.thanhnt.mailservice.global.enums;

public enum VerificationStatus {
  PENDING,
  VERIFIED,
  REJECTED,
  SUSPENDED,
  NEED_MORE_INFO;

  public boolean isPending() {

    return this == PENDING;
  }

  public boolean isVerified() {
    return this == VERIFIED;
  }

  public boolean isRejected() {
    return this == REJECTED;
  }

  public boolean isSuspended() {
    return this == SUSPENDED;
  }

  public boolean isNeedMoreInfo() {
    return this == NEED_MORE_INFO;
  }
}

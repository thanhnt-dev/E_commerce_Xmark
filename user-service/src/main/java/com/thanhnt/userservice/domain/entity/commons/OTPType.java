package com.thanhnt.userservice.domain.entity.commons;

public enum OTPType {
  REGISTER,
  FORGOT_PASSWORD,
  TWO_FA;

  public boolean isRegister() {
    return this == REGISTER;
  }

  public boolean isForgotPassword() {
    return this == FORGOT_PASSWORD;
  }

  public boolean isTwoFA() {
    return this == TWO_FA;
  }
}

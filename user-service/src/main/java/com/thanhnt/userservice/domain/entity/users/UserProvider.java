package com.thanhnt.userservice.domain.entity.users;

public enum UserProvider {
  NORMAL,
  GOOGLE;

  public boolean isNormal() {
    return this == NORMAL;
  }

  public boolean isGoogle() {
    return this == GOOGLE;
  }
}

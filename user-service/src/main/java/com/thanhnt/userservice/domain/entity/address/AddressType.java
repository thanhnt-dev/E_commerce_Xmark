package com.thanhnt.userservice.domain.entity.address;

public enum AddressType {
  HOME,
  OFFICE;

  public boolean isHome() {
    return this == HOME;
  }

  public boolean isOffice() {
    return this == OFFICE;
  }
}

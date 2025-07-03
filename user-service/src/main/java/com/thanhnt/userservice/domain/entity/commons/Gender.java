package com.thanhnt.userservice.domain.entity.commons;

public enum Gender {
  MALE,
  FEMALE,
  UNKNOWN;

  public boolean isMale() {
    return this == MALE;
  }

  public boolean isFemale() {
    return this == FEMALE;
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }
}

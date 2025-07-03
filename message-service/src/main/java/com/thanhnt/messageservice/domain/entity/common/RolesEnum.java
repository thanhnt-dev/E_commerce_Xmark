package com.thanhnt.messageservice.domain.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RolesEnum {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER"),
  ROLE_STORE_OWNER("ROLE_STORE_OWNER");

  private final String roleName;

  public static String getRoleName(String roleName) {
    for (RolesEnum role : RolesEnum.values()) {
      if (role.getRoleName().equals(roleName)) return role.getRoleName();
    }
    return null;
  }
}

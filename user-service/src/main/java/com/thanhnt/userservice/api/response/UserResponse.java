package com.thanhnt.userservice.api.response;

import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserResponse {
  private Long id;
  private String email;
  private String fullName;
  private String phone;
  private String avatar;
  private Long createdAt;
  private boolean status;
  private RolesEnum role;
}

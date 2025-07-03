package com.thanhnt.userservice.api.response;

import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import com.thanhnt.userservice.domain.entity.users.UserProvider;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginResponse {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String avatar;
  private String phone;
  private UserProvider provider;
  private String accessToken;
  private String refreshToken;
  private List<RolesEnum> roles;
  private String message;
}

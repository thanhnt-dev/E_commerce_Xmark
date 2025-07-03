package com.thanhnt.userservice.application.dto;

import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ValidateTokenDTO {
  private Long id;
  private String email;
  private String phone;
  private List<RolesEnum> roles;
}

package com.thanhnt.orderservice.application.dto;

import com.thanhnt.orderservice.domain.entity.common.RolesEnum;
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

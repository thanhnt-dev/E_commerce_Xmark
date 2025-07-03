package com.thanhnt.userservice.api.request;

import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RoleUpdateRequest {
  private Long id;
  private RolesEnum role;
}

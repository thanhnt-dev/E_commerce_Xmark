package com.thanhnt.storeservice.api.request;

import com.thanhnt.storeservice.domain.entity.common.RolesEnum;
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

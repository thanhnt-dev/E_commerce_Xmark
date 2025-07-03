package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;

public interface RoleService {
  Roles findRole(RolesEnum name);
}

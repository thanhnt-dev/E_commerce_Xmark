package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.application.exception.RoleException;
import com.thanhnt.userservice.application.service.RoleService;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import com.thanhnt.userservice.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Override
  public Roles findRole(RolesEnum name) {
    return roleRepository
        .findByRoleName(name)
        .orElseThrow(() -> new RoleException(ErrorCode.ROLE_NOT_FOUND));
  }
}

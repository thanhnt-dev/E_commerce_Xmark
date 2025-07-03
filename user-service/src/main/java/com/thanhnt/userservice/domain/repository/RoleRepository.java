package com.thanhnt.userservice.domain.repository;

import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
  Optional<Roles> findByRoleName(RolesEnum roleName);
}

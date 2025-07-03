package com.thanhnt.userservice.domain.entity.role;

import com.thanhnt.userservice.domain.entity.commons.BaseEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Roles extends BaseEntity implements Serializable {
  @Column(name = "role_name", nullable = false, length = 100)
  @Enumerated(EnumType.STRING)
  private RolesEnum roleName;
}

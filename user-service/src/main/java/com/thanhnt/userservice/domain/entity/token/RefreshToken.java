package com.thanhnt.userservice.domain.entity.token;

import com.thanhnt.userservice.domain.entity.commons.BaseEntity;
import com.thanhnt.userservice.domain.entity.users.Users;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity implements Serializable {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Users user;

  @Column(name = "refresh_token", nullable = false)
  private String refreshToken;
}

package com.thanhnt.userservice.infrastructure.security;

import com.thanhnt.userservice.domain.entity.users.UserProvider;
import com.thanhnt.userservice.domain.entity.users.Users;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SecurityUserDetail implements UserDetails {
  private Long id;
  private String email;
  private String phone;
  private String firstName;
  private String lastName;
  private String avatar;
  private UserProvider provider;
  private String password;
  private Boolean isFirstLogin;
  private Boolean isTwoFactor;
  private Boolean isActive;
  private Collection<? extends GrantedAuthority> authorities;

  public static SecurityUserDetail build(Users user, List<GrantedAuthority> authorities) {
    return SecurityUserDetail.builder()
        .id(user.getId())
        .email(user.getEmail())
        .phone(user.getPhone())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .avatar(user.getAvatar())
        .password(user.getPassword())
        .provider(user.getUserProvider())
        .isFirstLogin(user.isFirstLogin())
        .isTwoFactor(user.isEnableTwoFactor())
        .isActive(user.isActive())
        .authorities(authorities)
        .build();
  }

  public static SecurityUserDetail build(Users user) {
    return SecurityUserDetail.builder()
        .id(user.getId())
        .email(user.getEmail())
        .phone(user.getPhone())
        .password(user.getPassword())
        .isFirstLogin(user.isFirstLogin())
        .isTwoFactor(user.isEnableTwoFactor())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}

package com.thanhnt.userservice.domain.entity.users;

import com.thanhnt.userservice.domain.entity.address.Address;
import com.thanhnt.userservice.domain.entity.commons.BaseEntity;
import com.thanhnt.userservice.domain.entity.commons.Gender;
import com.thanhnt.userservice.domain.entity.pointtransaction.PointTransaction;
import com.thanhnt.userservice.domain.entity.role.Roles;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Users extends BaseEntity implements Serializable {

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "gender", length = 10)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "user_provider", length = 10)
  @Enumerated(EnumType.STRING)
  private UserProvider userProvider;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "public_id")
  private String publicId;

  @Column(name = "phone", unique = true, length = 10)
  private String phone;

  @Column(name = "date_of_birth", nullable = false)
  private Long dateOfBirth;

  @Column(name = "is_first_login", nullable = false)
  @Builder.Default
  private boolean isFirstLogin = true;

  @Column(name = "is_enable_two_factor", nullable = false)
  @Builder.Default
  private boolean isEnableTwoFactor = false;

  @Column(name = "point_balance", nullable = false)
  @Builder.Default
  private Integer pointBalance = 0;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private List<Roles> roles = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
  @Builder.Default
  private List<Address> addresses = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
  @Builder.Default
  private List<PointTransaction> pointTransactions = new ArrayList<>();

  public void addRole(Roles role) {
    this.roles.add(role);
  }

  public void loggedIn() {
    this.isFirstLogin = false;
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void toggle2FA(boolean isEnableTwoFactor) {
    this.isEnableTwoFactor = isEnableTwoFactor;
  }

  public void updateProfile(
      String firstName,
      String lastName,
      Gender gender,
      String phone,
      Long dateOfBirth,
      boolean isEnableTwoFactor) {
    if (firstName != null) {
      this.firstName = firstName;
    }
    if (lastName != null) {
      this.lastName = lastName;
    }
    if (dateOfBirth != null) {
      this.dateOfBirth = dateOfBirth;
    }
    this.isEnableTwoFactor = isEnableTwoFactor;
    this.gender = gender;
    this.phone = phone;
  }

  public void updateAvatar(String avatar, String publicId) {
    this.avatar = avatar;
    this.publicId = publicId;
  }

  public void addAddress(Address address) {
    this.addresses.add(address);
  }
}

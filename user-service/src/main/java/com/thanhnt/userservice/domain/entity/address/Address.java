package com.thanhnt.userservice.domain.entity.address;

import com.thanhnt.userservice.domain.entity.commons.BaseEntity;
import com.thanhnt.userservice.domain.entity.users.Users;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Address extends BaseEntity implements Serializable {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "phone", nullable = false, length = 10)
  private String phone;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AddressType type = AddressType.HOME;

  @Column(name = "detail")
  private String detail;

  @Column(name = "province_name", nullable = false)
  private String provinceName;

  @Column(name = "district_name", nullable = false)
  private String districtName;

  @Column(name = "ward_name", nullable = false)
  private String wardName;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Users users;

  public void updateAddress(
      String name,
      String phone,
      AddressType addressType,
      String detail,
      String ward,
      String district,
      String province) {
    this.name = name;
    this.phone = phone;
    this.type = addressType;
    this.detail = detail;
    this.wardName = ward;
    this.districtName = district;
    this.provinceName = province;
  }
}

package com.thanhnt.storeservice.domain.entity.shop;

import com.thanhnt.storeservice.domain.entity.common.BaseEntity;
import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import com.thanhnt.storeservice.domain.entity.shopfollower.ShopFollowers;
import com.thanhnt.storeservice.domain.entity.shopinfoasset.ShopInfoAssets;
import com.thanhnt.storeservice.domain.entity.shopproduct.ShopProducts;
import com.thanhnt.storeservice.domain.entity.shoprequest.ShopRequests;
import com.thanhnt.storeservice.domain.entity.shopsubscription.ShopSubscription;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shops")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop extends BaseEntity implements Serializable {

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "location", nullable = false, length = 100)
  private String location;

  @Column(name = "verification_status")
  @Enumerated(EnumType.STRING)
  private VerificationStatus verificationStatus;

  @Column(name = "identity_number", length = 12, unique = true, nullable = false)
  private String identityNumber;

  @Column(name = "owner_name", length = 50, nullable = false)
  private String ownerName;

  @Column(name = "email", length = 50, unique = true, nullable = false)
  private String email;

  @Column(name = "phone", length = 10, unique = true, nullable = false)
  private String phone;

  @Column(name = "tax_code", length = 10, unique = true, nullable = false)
  private String taxCode;

  @Column(name = "business_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private BusinessType businessType;

  @Column(name = "category")
  private Long category;

  @Column(name = "public_id")
  private String publicId;

  @Column(name = "owner_id", nullable = false, unique = true)
  private Long owner;

  @OneToMany(
      mappedBy = "shop",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopInfoAssets> shopInfoAssets = new ArrayList<>();

  @OneToMany(
      mappedBy = "shop",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopRequests> shopRequests = new ArrayList<>();

  @OneToMany(
      mappedBy = "shop",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopProducts> shopProducts = new ArrayList<>();

  @OneToMany(
      mappedBy = "shop",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopFollowers> shopFollowers = new ArrayList<>();

  @OneToMany(
      mappedBy = "shop",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopSubscription> shopSubscriptions = new ArrayList<>();

  public void updateVerifyStatus(VerificationStatus status, String reason, Long adminId) {
    this.verificationStatus = status;
    this.shopRequests.add(
        ShopRequests.builder().shop(this).reason(reason).adminId(adminId).build());
  }

  public void updateInfoShop(
      String name,
      String description,
      String location,
      String identityNumber,
      String ownerName,
      String email,
      String phone,
      String taxCode,
      BusinessType businessType,
      Long category) {
    this.name = name;
    this.description = description;
    this.location = location;
    this.identityNumber = identityNumber;
    this.ownerName = ownerName;
    this.email = email;
    this.phone = phone;
    this.taxCode = taxCode;
    this.businessType = businessType;
    this.category = category;
  }

  public void updateAvatar(String avatar, String publicId) {
    this.avatar = avatar;
    this.publicId = publicId;
  }
}

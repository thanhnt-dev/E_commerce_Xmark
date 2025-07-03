package com.thanhnt.productservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thanhnt.productservice.domain.entity.common.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShopSnapshotDTO {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("shopName")
  private String shopName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("avatar")
  private String avatar;

  @JsonProperty("businessType")
  private BusinessType businessType;
}

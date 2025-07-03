package com.thanhnt.storeservice.api.response;

import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfoResponse {
  private Long shopId;
  private String shopName;
  private String avatarUrl;
  private boolean isOnline;
  private Long lastOnline;
  private int productsCount;
  private int following;
  private int followers;
  private int responseRate;
  private String responseTime;
  private double rating;
  private Long joinedSince;
  private BusinessType businessType;
}

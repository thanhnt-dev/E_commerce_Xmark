package com.thanhnt.storeservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AdminSellerStatsResponse {
  private Long totalSellers;
  private Double growthPercentage;
  private Long totalActiveSellers;
  private Long totalInactiveSellers;
  private Long lastMonthSellers;
}

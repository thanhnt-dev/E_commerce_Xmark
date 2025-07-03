package com.thanhnt.storeservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AdminSellerStatsDTO {
  private Long totalSellers;
  private Double growthPercentage;
  private Long totalActiveSellers;
  private Long totalInactiveSellers;
  private Long lastMonthSellers;
  private Long totalSellersPendingApproval;
  private Long totalSellersRejected;
  private Long totalSellersApproved;
}

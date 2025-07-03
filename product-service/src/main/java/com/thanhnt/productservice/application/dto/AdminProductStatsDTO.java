package com.thanhnt.productservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AdminProductStatsDTO {
  private Long totalProducts;
  private Double growthPercentage;
  private Long lastMonthProducts;
  private Long totalProductsPendingApproval;
  private Long totalProductsRejected;
  private Long totalProductsApproved;
}

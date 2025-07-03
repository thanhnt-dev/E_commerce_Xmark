package com.thanhnt.productservice.api.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AdminProductStatsResponse {
  private Long totalProducts;
  private Double growthPercentage;
  private Long lastMonthProducts;
}

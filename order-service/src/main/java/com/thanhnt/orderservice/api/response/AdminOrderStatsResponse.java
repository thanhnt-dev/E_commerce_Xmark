package com.thanhnt.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AdminOrderStatsResponse {
  private Long totalOrders;
  private Long totalPrice;
  private Double averagePrice;
  private Long totalOrdersToday;
  private Long totalOrdersThisWeek;
  private Long totalOrdersThisMonth;
  private Long totalOrdersThisYear;
  private Long totalOrdersLastMonth;
  private Double percentageChangeFromLastMonth;
}

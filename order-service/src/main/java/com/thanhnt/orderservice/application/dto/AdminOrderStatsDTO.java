package com.thanhnt.orderservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AdminOrderStatsDTO {
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

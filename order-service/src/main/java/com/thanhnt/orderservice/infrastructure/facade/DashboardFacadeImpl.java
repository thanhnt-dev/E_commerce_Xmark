package com.thanhnt.orderservice.infrastructure.facade;

import com.thanhnt.orderservice.api.facade.DashboardFacade;
import com.thanhnt.orderservice.api.response.AdminOrderStatsResponse;
import com.thanhnt.orderservice.api.response.BaseResponse;
import com.thanhnt.orderservice.application.dto.AdminOrderStatsDTO;
import com.thanhnt.orderservice.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardFacadeImpl implements DashboardFacade {
  private final OrderService orderService;

  @Override
  public BaseResponse<AdminOrderStatsResponse> getAdminOrderStats() {
    log.info("Fetching admin order stats");
    try {
      AdminOrderStatsDTO stats = orderService.getOrderStatsByAdmin();
      if (stats == null) {
        log.warn("No order stats found");
        return BaseResponse.build(null, false);
      }
      log.info("Successfully fetched admin order stats: {}", stats);
      return BaseResponse.build(
          AdminOrderStatsResponse.builder()
              .totalOrders(stats.getTotalOrders())
              .totalPrice(stats.getTotalPrice())
              .averagePrice(stats.getAveragePrice())
              .totalOrdersToday(stats.getTotalOrdersToday())
              .totalOrdersThisWeek(stats.getTotalOrdersThisWeek())
              .totalOrdersThisMonth(stats.getTotalOrdersThisMonth())
              .totalOrdersThisYear(stats.getTotalOrdersThisYear())
              .totalOrdersLastMonth(stats.getTotalOrdersLastMonth())
              .percentageChangeFromLastMonth(stats.getPercentageChangeFromLastMonth())
              .build(),
          true);
    } catch (Exception e) {
      log.error("Error fetching admin order stats", e);
      return BaseResponse.build(null, false);
    }
  }
}

package com.thanhnt.storeservice.infrastructure.facade;

import com.thanhnt.storeservice.api.facade.DashboardFacade;
import com.thanhnt.storeservice.api.response.AdminSellerStatsResponse;
import com.thanhnt.storeservice.api.response.BaseResponse;
import com.thanhnt.storeservice.application.dto.AdminSellerStatsDTO;
import com.thanhnt.storeservice.application.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardFacadeImpl implements DashboardFacade {
  private final ShopService shopService;

  @Override
  public BaseResponse<AdminSellerStatsResponse> getAdminSellerStats() {
    log.info("Getting admin seller stats");
    AdminSellerStatsDTO stats = shopService.getAdminSellerStats();
    if (stats == null) {
      log.error("Not able to retrieve admin seller stats");
      return BaseResponse.build(null, false);
    }
    log.info("Admin seller stats retrieved successfully");
    return BaseResponse.build(
        AdminSellerStatsResponse.builder()
            .totalSellers(stats.getTotalSellers())
            .growthPercentage(stats.getGrowthPercentage())
            .totalActiveSellers(stats.getTotalActiveSellers())
            .totalInactiveSellers(stats.getTotalInactiveSellers())
            .lastMonthSellers(stats.getLastMonthSellers())
            .build(),
        true);
  }
}

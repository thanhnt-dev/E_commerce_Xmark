package com.thanhnt.productservice.infrastructure.facade;

import com.thanhnt.productservice.api.facade.DashboardFacade;
import com.thanhnt.productservice.api.response.AdminProductStatsResponse;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.application.dto.AdminProductStatsDTO;
import com.thanhnt.productservice.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardFacadeImpl implements DashboardFacade {
  private final ProductService productService;

  @Override
  public BaseResponse<AdminProductStatsResponse> getAdminProductStats() {
    log.info("Getting admin product stats");
    AdminProductStatsDTO stats = productService.getAdminProductStats();
    if (stats == null) {
      log.error("Not able to retrieve admin product stats");
      return BaseResponse.build(null, false);
    }
    log.info("Admin product stats retrieved successfully");
    return BaseResponse.build(
        AdminProductStatsResponse.builder()
            .totalProducts(stats.getTotalProducts())
            .growthPercentage(stats.getGrowthPercentage())
            .lastMonthProducts(stats.getLastMonthProducts())
            .build(),
        true);
  }
}

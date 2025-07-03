package com.thanhnt.orderservice.infrastructure.rest.controller;

import com.thanhnt.orderservice.api.facade.DashboardFacade;
import com.thanhnt.orderservice.api.response.AdminOrderStatsResponse;
import com.thanhnt.orderservice.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardFacade dashboardFacade;

  @GetMapping("/admin/order-stats")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get admin order statistics",
      tags = {"Dashboard APIs"})
  public BaseResponse<AdminOrderStatsResponse> getAdminOrderStats() {
    return this.dashboardFacade.getAdminOrderStats();
  }
}

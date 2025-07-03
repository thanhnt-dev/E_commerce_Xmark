package com.thanhnt.productservice.infrastructure.rest.controller;

import com.thanhnt.productservice.api.facade.DashboardFacade;
import com.thanhnt.productservice.api.response.AdminProductStatsResponse;
import com.thanhnt.productservice.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardFacade dashboardFacade;

  @GetMapping("/admin/product-stats")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get admin product stats with Dashboard",
      tags = {"Dashboard APIs"})
  public BaseResponse<AdminProductStatsResponse> getAdminProductStats() {
    return this.dashboardFacade.getAdminProductStats();
  }
}

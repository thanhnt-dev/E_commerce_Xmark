package com.thanhnt.storeservice.infrastructure.rest.controller;

import com.thanhnt.storeservice.api.facade.DashboardFacade;
import com.thanhnt.storeservice.api.response.AdminSellerStatsResponse;
import com.thanhnt.storeservice.api.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardFacade dashboardFacade;

  @GetMapping("/admin/stats")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get admin seller stats",
      tags = {"Dashboard APIs"})
  public BaseResponse<AdminSellerStatsResponse> getAdminSellerStats() {
    log.info("Fetching admin seller stats");
    return this.dashboardFacade.getAdminSellerStats();
  }
}

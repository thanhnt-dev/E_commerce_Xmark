package com.thanhnt.userservice.infrastructure.rest.controller;

import com.thanhnt.userservice.api.facade.DashboardFacade;
import com.thanhnt.userservice.api.request.UserCriteria;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.PaginationResponse;
import com.thanhnt.userservice.api.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
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

  @GetMapping("/admin/stats")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get account statistics for admin",
      tags = {"Dashboard APIs"})
  public BaseResponse<PaginationResponse<List<UserResponse>>> getAdminAccountStats(
      UserCriteria criteria) {
    return this.dashboardFacade.getUserByFilter(criteria);
  }
}

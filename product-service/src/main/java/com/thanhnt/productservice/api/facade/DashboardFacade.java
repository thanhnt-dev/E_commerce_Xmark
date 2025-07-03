package com.thanhnt.productservice.api.facade;

import com.thanhnt.productservice.api.response.AdminProductStatsResponse;
import com.thanhnt.productservice.api.response.BaseResponse;

public interface DashboardFacade {
  BaseResponse<AdminProductStatsResponse> getAdminProductStats();
}

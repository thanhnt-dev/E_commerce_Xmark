package com.thanhnt.orderservice.api.facade;

import com.thanhnt.orderservice.api.response.AdminOrderStatsResponse;
import com.thanhnt.orderservice.api.response.BaseResponse;

public interface DashboardFacade {
  BaseResponse<AdminOrderStatsResponse> getAdminOrderStats();
}

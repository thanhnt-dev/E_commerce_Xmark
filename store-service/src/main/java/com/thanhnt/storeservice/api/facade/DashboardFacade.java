package com.thanhnt.storeservice.api.facade;

import com.thanhnt.storeservice.api.response.AdminSellerStatsResponse;
import com.thanhnt.storeservice.api.response.BaseResponse;

public interface DashboardFacade {
  BaseResponse<AdminSellerStatsResponse> getAdminSellerStats();
}

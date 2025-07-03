package com.thanhnt.userservice.api.facade;

import com.thanhnt.userservice.api.request.UserCriteria;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.PaginationResponse;
import com.thanhnt.userservice.api.response.UserResponse;
import java.util.List;

public interface DashboardFacade {
  BaseResponse<PaginationResponse<List<UserResponse>>> getUserByFilter(UserCriteria userCriteria);
}

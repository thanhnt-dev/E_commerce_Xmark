package com.thanhnt.orderservice.api.facade;

import com.thanhnt.orderservice.api.request.CreateMomoRequest;
import com.thanhnt.orderservice.api.response.BaseResponse;
import com.thanhnt.orderservice.api.response.CreateMomoResponse;
import java.util.Map;

public interface MomoFacade {
  BaseResponse<CreateMomoResponse> createMomoPayment(CreateMomoRequest request);

  BaseResponse<Void> verifyMomoPayment(Map<String, String> requestParams);
}

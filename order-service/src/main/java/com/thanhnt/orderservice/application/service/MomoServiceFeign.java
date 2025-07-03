package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.api.request.CreateMomoRequest;
import com.thanhnt.orderservice.api.response.CreateMomoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo", url = "${momo.endpoint}")
public interface MomoServiceFeign {
  @PostMapping("/create")
  CreateMomoResponse createMomoPayment(@RequestBody CreateMomoRequest request);
}

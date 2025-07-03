package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.application.dto.CustomerInfoDTO;
import com.thanhnt.orderservice.application.dto.ValidateTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceFeign {
  @GetMapping(value = "/api/v1/account/validate-token", headers = "API_SECRET_HEADER=secret1403")
  ValidateTokenDTO validateToken(@RequestHeader(name = "Authorization") String token);

  @GetMapping(value = "/api/v1/users/customer-info/{id}", headers = "API_SECRET_HEADER=secret1403")
  CustomerInfoDTO getCustomerInfo(@PathVariable Long id);
}

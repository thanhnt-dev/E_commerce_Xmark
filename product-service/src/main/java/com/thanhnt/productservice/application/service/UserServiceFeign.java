package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.ValidateTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceFeign {
  @GetMapping(value = "/api/v1/account/validate-token", headers = "API_SECRET_HEADER=secret1403")
  ValidateTokenDTO validateToken(@RequestHeader(name = "Authorization") String token);
}

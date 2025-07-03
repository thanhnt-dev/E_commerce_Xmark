package com.thanhnt.storeservice.application.service;

import com.thanhnt.storeservice.api.request.RoleUpdateRequest;
import com.thanhnt.storeservice.application.dto.StoreOwnerDetailDTO;
import com.thanhnt.storeservice.application.dto.ValidateTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceFeign {
  @GetMapping(value = "/api/v1/account/validate-token", headers = "API_SECRET_HEADER=secret1403")
  ValidateTokenDTO validateToken(@RequestHeader(name = "Authorization") String token);

  @GetMapping(value = "/api/v1/users/store-owner/{id}", headers = "API_SECRET_HEADER=secret1403")
  StoreOwnerDetailDTO getOwnerById(@PathVariable Long id);

  @PostMapping(value = "/api/v1/users/permission", headers = "API_SECRET_HEADER=secret1403")
  Boolean updateUserRole(@RequestBody RoleUpdateRequest request);
}

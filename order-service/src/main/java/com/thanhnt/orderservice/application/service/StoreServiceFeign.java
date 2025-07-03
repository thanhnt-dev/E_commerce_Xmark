package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.application.dto.ShopSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store-service")
public interface StoreServiceFeign {
  @GetMapping(value = "/api/v1/stores/valid-store/{id}", headers = "API_SECRET_HEADER=secret1403")
  ShopSnapshotDTO checkValidShopById(@PathVariable Long id);

  @GetMapping(value = "/api/v1/stores/info/{id}", headers = "API_SECRET_HEADER=secret1403")
  ShopSnapshotDTO getShopInfoById(@PathVariable Long id);
}

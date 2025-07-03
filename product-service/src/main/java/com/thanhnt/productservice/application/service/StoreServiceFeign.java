package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.productservice.application.dto.UpdateProductShopDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "store-service")
public interface StoreServiceFeign {
  @GetMapping(value = "/api/v1/stores/valid-store/{id}", headers = "API_SECRET_HEADER=secret1403")
  ShopSnapshotDTO checkValidShopById(@PathVariable Long id);

  @GetMapping(value = "/api/v1/stores/info/{id}", headers = "API_SECRET_HEADER=secret1403")
  ShopSnapshotDTO getShopInfoById(@PathVariable Long id);

  @PostMapping(value = "/api/v1/stores/add_product_shop")
  Boolean updateProductShop(@RequestBody UpdateProductShopDTO updateProductShopDTO);
}

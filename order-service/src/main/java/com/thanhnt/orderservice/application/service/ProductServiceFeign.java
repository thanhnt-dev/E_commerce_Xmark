package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.api.request.ValidateOrderItemRequest;
import com.thanhnt.orderservice.application.dto.ProductValidDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "product-service")
public interface ProductServiceFeign {
  @PostMapping(value = "/api/v1/products/validate", headers = "API_SECRET_HEADER=secret1403")
  List<ProductValidDTO> validateProducts(List<ValidateOrderItemRequest> validateOrderItemRequests);

  @PostMapping(value = "/api/v1/products/update_quantity", headers = "API_SECRET_HEADER=secret1403")
  boolean updateProductQuantity(List<ValidateOrderItemRequest> validateOrderItemRequests);
}

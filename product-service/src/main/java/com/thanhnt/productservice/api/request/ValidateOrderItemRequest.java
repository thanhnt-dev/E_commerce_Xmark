package com.thanhnt.productservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ValidateOrderItemRequest {
  private Long productVariantId;
  private Integer quantity;
  private Integer version;
}

package com.thanhnt.orderservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderItemResponse {
  private Long productVariantId;
  private Integer quantity;
  private String imageUrl;
  private String productName;
  private Long price;
}

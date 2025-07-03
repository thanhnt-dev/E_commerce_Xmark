package com.thanhnt.storeservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductShopDTO {
  private Long shopId;
  private Long productId;
}

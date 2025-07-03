package com.thanhnt.productservice.api.response;

import com.thanhnt.productservice.domain.entity.common.ProductCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductVariantDetailResponse {
  private Long id;
  private String size;
  private Long resalePrice;
  private Long originalPrice;
  private Integer quantity;
  private ProductCondition condition;
  private boolean isActive;
  private Integer version;
}

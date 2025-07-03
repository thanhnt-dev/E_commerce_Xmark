package com.thanhnt.productservice.api.response;

import com.thanhnt.productservice.domain.entity.common.ProductCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
  private Long id;
  private String productCode;
  private String name;
  private ProductCondition productCondition;
  private Long resalePrice;
  private String imageUrl;
  private Long shopId;
  private boolean isActive;
  private String shopName;
  private Integer version;
}

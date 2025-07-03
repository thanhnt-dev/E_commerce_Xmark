package com.thanhnt.productservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProductLogDetailDTO {
  private Long userId;
  private Long productId;
  private String productName;
  private String description;
  private Long price;
  private String productImageUrl;
  private Long subCategoryId;
  private String productSubCategory;
  private String productBrand;
  private Long viewCount;
}

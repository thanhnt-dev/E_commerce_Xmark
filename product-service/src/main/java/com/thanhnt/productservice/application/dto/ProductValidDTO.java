package com.thanhnt.productservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductValidDTO {
  private Long productValidId;
  private String productName;
  private String productCode;
  private Integer quantity;
  private String imageUrl;
  private Long price;
  private boolean isValid;
}

package com.thanhnt.orderservice.application.dto;

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
  private String imageUrl;
  private Integer quantity;
  private Long price;
  private boolean isValid;
}

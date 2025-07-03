package com.thanhnt.productservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponse {
  private Long id;
  private String name;
  private String description;
  private boolean isActive;
}

package com.thanhnt.productservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryChildResponse {
  private Long id;
  private String name;
  private String description;
  private boolean isActive;
  //  private List<SubCategoryResponse> subCategoryResponse;
}

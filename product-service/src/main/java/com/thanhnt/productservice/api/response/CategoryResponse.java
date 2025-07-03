package com.thanhnt.productservice.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
  private Long id;
  private String name;
  private String description;
  private boolean isActive;
  private List<CategoryChildResponse> children;
}

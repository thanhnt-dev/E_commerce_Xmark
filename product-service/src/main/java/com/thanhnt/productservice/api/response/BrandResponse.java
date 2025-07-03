package com.thanhnt.productservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BrandResponse {
  private Long id;
  private String name;
  private String description;
  private String logoUrl;
}

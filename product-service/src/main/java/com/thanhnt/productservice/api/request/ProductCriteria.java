package com.thanhnt.productservice.api.request;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductCriteria extends BaseCriteria {
  private Integer currentPage;
  private Integer pageSize;
  private Long minPrice;
  private Long maxPrice;
  private Long brandId;
  private Long shopId;
  private String sort;
  private Long subCategory;
  private String search;
  private ProductApprovalStatus approvalStatus;
}

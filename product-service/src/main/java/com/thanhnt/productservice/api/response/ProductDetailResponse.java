package com.thanhnt.productservice.api.response;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.common.SaleStatus;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDetailResponse {
  private Long id;
  private String productCode;
  private String productName;
  private String productDescription;
  private Long brandId;
  private String productBrand;
  private Long viewCount;
  private Long subCategoryId;
  private String productSubCategory;
  private List<String> productImageUrl;
  private ProductApprovalStatus productApprovalStatus;
  private Long productApprovalBy;
  private String productApprovalByName;
  private String productOrigin;
  private String productStory;
  private HashMap<String, String> productDetails;
  private SaleStatus saleStatus;
  private String reasonReject;
  private List<ProductVariantDetailResponse> productVariants;
  private Long createdAt;
  private Long updatedAt;
  private Long shopId;
  private Integer version;
}

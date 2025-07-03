package com.thanhnt.productservice.domain.entity.product;

import com.thanhnt.productservice.application.helper.JsonConverter;
import com.thanhnt.productservice.domain.entity.brand.Brand;
import com.thanhnt.productservice.domain.entity.common.BaseEntity;
import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.common.SaleStatus;
import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;
import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Proxy;

@Entity
@Getter
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Proxy(lazy = false)
public class Product extends BaseEntity implements Serializable {

  @Column(name = "product_sku", length = 15, nullable = false, unique = true)
  private String productSku;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(name = "description")
  private String description;

  @Column(name = "story")
  private String story;

  @Column(name = "view_count", nullable = false)
  @Builder.Default
  private Long viewCount = 0L;

  @Column(name = "origin", nullable = false)
  private String origin;

  @Column(name = "product_details", columnDefinition = "jsonb")
  @Convert(converter = JsonConverter.class)
  @ColumnTransformer(read = "product_details::jsonb", write = "?::jsonb")
  private HashMap<String, String> productDetails;

  @Enumerated(EnumType.STRING)
  @Column(name = "sale_status")
  private SaleStatus saleStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id", nullable = false)
  private Brand brand;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sub_category_id", nullable = false)
  private SubCategory subCategory;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ProductAsset> productAssets = new ArrayList<>();

  public void addProductAsset(List<ProductAsset> productAssets) {
    this.productAssets.addAll(productAssets);
  }

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ProductVariant> productVariants = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "product_approval_status")
  private ProductApprovalStatus productApprovalStatus;

  @Column(name = "product_approval_by")
  private Long productApprovalBy;

  @Column(name = "reason_reject")
  private String reasonReject;

  public void updateProduct(
      String productName,
      String description,
      String story,
      String origin,
      HashMap<String, String> productDetails,
      SaleStatus saleStatus,
      Brand brand,
      SubCategory subCategory,
      Long shopId,
      ProductApprovalStatus productApprovalStatus,
      Long productApprovalBy,
      String reasonReject) {
    if (productName != null) {
      this.productName = productName;
    }
    if (description != null) {
      this.description = description;
    }
    if (story != null) {
      this.story = story;
    }
    if (origin != null) {
      this.origin = origin;
    }
    if (productDetails != null) {
      this.productDetails = productDetails;
    }
    if (saleStatus != null) {
      this.saleStatus = saleStatus;
    }
    if (brand != null) {
      this.brand = brand;
    }
    if (subCategory != null) {
      this.subCategory = subCategory;
    }
    if (shopId != null) {
      this.shopId = shopId;
    }
    if (productApprovalStatus != null) {
      this.productApprovalStatus = productApprovalStatus;
    }
    if (productApprovalBy != null) {
      this.productApprovalBy = productApprovalBy;
    }
    if (reasonReject != null) {
      this.reasonReject = reasonReject;
    }
  }

  public void updateProductApprovalStatus(
      ProductApprovalStatus productApprovalStatus, String reasonReject, Long productApprovalBy) {
    this.productApprovalStatus = productApprovalStatus;
    if (reasonReject != null) {
      this.reasonReject = reasonReject;
    }
    if (productApprovalBy != null) {
      this.productApprovalBy = productApprovalBy;
    }
  }
}

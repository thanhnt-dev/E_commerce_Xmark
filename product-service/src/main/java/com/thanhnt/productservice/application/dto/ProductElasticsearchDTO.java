package com.thanhnt.productservice.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.common.SaleStatus;
import jakarta.persistence.Id;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "product")
public class ProductElasticsearchDTO {
  @Id private String id;

  @Field(type = FieldType.Keyword, name = "productSku")
  private String productSku;

  @Field(type = FieldType.Text, name = "productName")
  private String productName;

  @Field(type = FieldType.Text, name = "description")
  private String description;

  @Field(type = FieldType.Text, name = "story")
  private String story;

  @Field(type = FieldType.Long, name = "viewCount")
  private Long viewCount;

  @Field(type = FieldType.Keyword, name = "origin")
  private String origin;

  @Field(type = FieldType.Object, name = "productDetails")
  private Map<String, String> productDetails;

  @Field(type = FieldType.Keyword, name = "saleStatus")
  private SaleStatus saleStatus;

  @Field(type = FieldType.Keyword, name = "brandName")
  private String brandName;

  @Field(type = FieldType.Keyword, name = "brandId")
  private Long brandId;

  @Field(type = FieldType.Long, name = "shopId")
  private Long shopId;

  @Field(type = FieldType.Text, name = "shopName")
  private String shopName;

  @Field(type = FieldType.Nested, name = "productAssets")
  private List<ProductAssetElasticsearchDTO> productAssets;

  @Field(type = FieldType.Nested, name = "variants")
  private List<ProductVariantElasticsearchDTO> variants;

  @Field(type = FieldType.Keyword, name = "subCategoryId")
  private Long subCategoryId;

  @Field(type = FieldType.Keyword, name = "subCategoryName")
  private String subCategoryName;

  @Field(type = FieldType.Keyword, name = "productApprovalStatus")
  private ProductApprovalStatus productApprovalStatus;

  @Field(type = FieldType.Long, name = "productApprovalBy")
  private Long productApprovalBy;

  @Field(type = FieldType.Text, name = "reasonReject")
  private String reasonReject;

  @Field(type = FieldType.Long, name = "createdAt")
  private Long createdAt;

  @Field(type = FieldType.Long, name = "updatedAt")
  private Long updatedAt;

  @Field(type = FieldType.Long, name = "isActive")
  private Boolean isActive;

  @Field(type = FieldType.Integer, name = "version")
  private Integer version;
}

package com.thanhnt.productservice.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanhnt.productservice.domain.entity.common.ProductCondition;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "product-variant")
public class ProductVariantElasticsearchDTO {
  @Id private String id;

  @Field(type = FieldType.Keyword, name = "productSize")
  private String productSize;

  @Field(type = FieldType.Integer, name = "quantity")
  private Integer quantity;

  @Field(type = FieldType.Long, name = "originalPrice")
  private Long originalPrice;

  @Field(type = FieldType.Long, name = "resalePrice")
  private Long resalePrice;

  @Field(type = FieldType.Keyword, name = "condition")
  private ProductCondition condition;

  @Field(type = FieldType.Boolean, name = "isActive")
  private boolean isActive;

  @Field(type = FieldType.Integer, name = "version")
  private Integer version;
}

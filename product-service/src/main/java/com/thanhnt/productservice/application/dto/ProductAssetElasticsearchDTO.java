package com.thanhnt.productservice.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Document(indexName = "product-asset")
public class ProductAssetElasticsearchDTO {
  @Id private String id;

  @Field(type = FieldType.Keyword, name = "mediaKey")
  private String mediaKey;

  @Field(type = FieldType.Keyword, name = "publicId")
  private String publicId;
}

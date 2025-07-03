package com.thanhnt.productservice.domain.logs;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_logs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDetail {
  @Id private String id;
  private Long userId;
  private Long productId;
  private String productName;
  private String description;
  private Long price;
  private String productImageUrl;
  private Long subCategoryId;
  private String productSubCategory;
  private String productBrand;
  @Builder.Default private LocalDate createdAt = LocalDate.now();
}

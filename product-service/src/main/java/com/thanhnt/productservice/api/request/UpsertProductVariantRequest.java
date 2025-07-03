package com.thanhnt.productservice.api.request;

import com.thanhnt.productservice.domain.entity.common.ProductCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpsertProductVariantRequest {
  private Long productVariantId;

  private String productSize;

  @NotNull(message = "Product quantity is require")
  @Schema(description = "Product quantity", example = "1")
  private Integer quantity;

  @NotNull(message = "Product originalPrice is require")
  @Schema(description = "Product originalPrice", example = "1000000")
  private Long originalPrice;

  @NotNull(message = "Product resalePrice is require")
  @Schema(description = "Product resalePrice", example = "1000000")
  private Long resalePrice;

  private ProductCondition productCondition;
}

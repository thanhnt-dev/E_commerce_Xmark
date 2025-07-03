package com.thanhnt.productservice.api.request;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.common.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpsertProductRequest {
  private Long id;

  @NotNull(message = "Product name is require")
  @NotEmpty(message = "Product name can not be empty")
  @Schema(description = "Product name")
  private String name;

  private String description;

  private String story;

  private SaleStatus saleStatus;

  @NotNull(message = "Product origin is require")
  @NotEmpty(message = "Product origin can not be empty")
  @Schema(description = "Product origin")
  private String origin;

  @NotNull(message = "Product details is require")
  @NotEmpty(message = "Product details can not be empty")
  @Schema(description = "Product details")
  private HashMap<String, String> productDetails;

  @NotNull(message = "Product brand is require")
  @Schema(description = "Product Brand", example = "1")
  private Long brandId;

  @NotNull(message = "Product subcategory is require")
  @Schema(description = "Product subcategory", example = "1")
  private Long subcategory;

  private RequestType requestType;

  @NotNull(message = "Product variant is require")
  @Schema(description = "Product variant")
  @Size(min = 1, message = "At least one variant is required")
  @Valid
  private List<UpsertProductVariantRequest> variantRequestList;

  private ProductApprovalStatus approvalStatus;
  private String reasonReject;
  private Long productApprovalBy;
}

package com.thanhnt.storeservice.api.request;

import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpsertShopRequest {
  @NotNull(message = "Shop name is required")
  @NotBlank(message = "Shop name cannot be blank")
  @Schema(description = "Shop Name", example = "ABC Shop")
  private String shopName;

  @NotNull(message = "Shop location is required")
  @NotBlank(message = "Shop location cannot be blank")
  @Schema(description = "Shop Location", example = "123 ABC Street")
  private String location;

  @NotNull(message = "Shop identity number is required")
  @NotBlank(message = "Shop identity number cannot be blank")
  @Size(min = 12, max = 12, message = "Identity number must be 12 numbers")
  private String identityNumber;

  @NotNull(message = "Owner name is required")
  @NotBlank(message = "Owner name cannot be blank")
  @Schema(description = "Owner name", example = "Nguyen Van A")
  private String ownerName;

  private BusinessType businessType;

  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be blank")
  @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "The email format is incorrect.")
  @Schema(description = "email", example = "email@email.com")
  private String email;

  @Schema(description = "description", example = "This is a shop")
  @NotNull(message = "Description is required")
  @NotBlank(message = "Description cannot be blank")
  private String description;

  @NotNull(message = "Phone is required")
  @NotBlank(message = "Phone cannot be blank")
  @Pattern(regexp = "^\\d{10}$", message = "The phone number must be 10 digits.")
  @Schema(description = "phone", example = "0123456789")
  private String phone;

  @NotNull(message = "Tax code is required")
  @NotBlank(message = "Tax code cannot be blank")
  @Schema(description = "Tax code", example = "123456789")
  private String taxCode;

  private Long category;

  @AssertTrue(message = "Category cannot be null or empty when business type is OFFICE")
  public boolean validateCategories() {
    if (businessType.isBusinessType()) {
      return category != null;
    }
    return true;
  }
}

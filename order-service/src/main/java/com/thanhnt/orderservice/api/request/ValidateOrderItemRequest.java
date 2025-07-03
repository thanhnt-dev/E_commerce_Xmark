package com.thanhnt.orderservice.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ValidateOrderItemRequest {
  @NotNull(message = "Product variant ID cannot be null")
  private Long productVariantId;

  @NotNull(message = "Quantity cannot be null")
  private Integer quantity;

  @NotNull(message = "Price cannot be null")
  private Integer version;
}

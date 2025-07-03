package com.thanhnt.orderservice.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ValidateOrderRequest {
  @NotNull(message = "Shop ID cannot be null")
  @Schema(description = "ID of the shop where the order is placed", example = "12345")
  private Long shopId;

  private String shippingAddress;
  private BigDecimal shippingFee;
  private Long voucherUserId;
  private String voucherCode;
  private String note;
  private String customerName;
  private String customerPhone;
  List<ValidateOrderItemRequest> orderItems;
}

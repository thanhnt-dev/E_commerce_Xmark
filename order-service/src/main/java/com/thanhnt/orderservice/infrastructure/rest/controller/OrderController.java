package com.thanhnt.orderservice.infrastructure.rest.controller;

import com.thanhnt.orderservice.api.facade.OrderFacade;
import com.thanhnt.orderservice.api.request.OrderCriteria;
import com.thanhnt.orderservice.api.request.ValidateOrderRequest;
import com.thanhnt.orderservice.api.response.*;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderFacade orderFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Create  an order",
      tags = {"Orders APIs"})
  public BaseResponse<OrderDetailResponse> upsertProduct(
      @RequestBody @Valid ValidateOrderRequest request) {
    return this.orderFacade.validateOrderRequest(request);
  }

  @PutMapping("/{orderCode}/cancel")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Cancel an order",
      tags = {"Orders APIs"})
  public BaseResponse<Void> cancelOrder(@PathVariable String orderCode) {
    return this.orderFacade.cancelOrder(orderCode);
  }

  @PutMapping("/{orderCode}/confirm")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Confirm an order",
      tags = {"Orders APIs"})
  public BaseResponse<ConfirmOrderResponse> confirmOrder(
      @PathVariable String orderCode, @RequestParam("paymentType") PaymentType paymentType) {
    return this.orderFacade.confirmOrder(orderCode, paymentType);
  }

  @GetMapping("/shop")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all orders of a shop",
      tags = {"Orders APIs"})
  BaseResponse<PaginationResponse<List<OrderResponse>>> getAllOrdersOfShop(OrderCriteria criteria) {
    return this.orderFacade.getOrdersWithFilterByShop(criteria);
  }

  @GetMapping("/shop/{shopId}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all orders of a shop by admin",
      tags = {"Orders APIs"})
  BaseResponse<PaginationResponse<List<OrderResponse>>> getAllOrdersOfShopByAdmin(
      OrderCriteria criteria, @PathVariable Long shopId) {
    return this.orderFacade.getOrdersWithFilterByAdmin(criteria, shopId);
  }

  @GetMapping("/{orderCode}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get order detail by order code",
      tags = {"Orders APIs"})
  public BaseResponse<OrderDetailResponse> getOrderDetailByOrderCode(
      @PathVariable String orderCode) {
    return this.orderFacade.getOrderDetailByOrderCode(orderCode);
  }

  @PutMapping("/{orderCode}/approve-by-shop")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Shop owner approves an order",
      tags = {"Orders APIs"})
  public BaseResponse<Void> approveOrderByShop(@PathVariable String orderCode) {
    return this.orderFacade.approveOrder(orderCode);
  }

  @GetMapping("/customer")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all orders of a customer",
      tags = {"Orders APIs"})
  public BaseResponse<PaginationResponse<List<OrderCustomerResponse>>> getOrdersByCustomer(
      OrderCriteria criteria) {
    return this.orderFacade.getOrdersByCustomer(criteria);
  }
}

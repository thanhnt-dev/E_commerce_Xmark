package com.thanhnt.orderservice.api.facade;

import com.thanhnt.orderservice.api.request.OrderCriteria;
import com.thanhnt.orderservice.api.request.ValidateOrderRequest;
import com.thanhnt.orderservice.api.response.*;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import java.util.List;

public interface OrderFacade {
  BaseResponse<OrderDetailResponse> validateOrderRequest(ValidateOrderRequest request);

  BaseResponse<Void> cancelOrder(String orderCode);

  BaseResponse<ConfirmOrderResponse> confirmOrder(String orderCode, PaymentType paymentType);

  BaseResponse<PaginationResponse<List<OrderResponse>>> getOrdersWithFilterByShop(
      OrderCriteria criteria);

  BaseResponse<OrderDetailResponse> getOrderDetailByOrderCode(String orderCode);

  BaseResponse<Void> approveOrder(String orderCode);

  BaseResponse<PaginationResponse<List<OrderResponse>>> getOrdersWithFilterByAdmin(
      OrderCriteria criteria, Long shopId);

  BaseResponse<PaginationResponse<List<OrderCustomerResponse>>> getOrdersByCustomer(
      OrderCriteria criteria);
}

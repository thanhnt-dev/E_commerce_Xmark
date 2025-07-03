package com.thanhnt.orderservice.infrastructure.facade;

import com.thanhnt.orderservice.api.facade.OrderFacade;
import com.thanhnt.orderservice.api.request.OrderCriteria;
import com.thanhnt.orderservice.api.request.ValidateOrderItemRequest;
import com.thanhnt.orderservice.api.request.ValidateOrderRequest;
import com.thanhnt.orderservice.api.response.*;
import com.thanhnt.orderservice.application.dto.CustomerInfoDTO;
import com.thanhnt.orderservice.application.dto.ProductValidDTO;
import com.thanhnt.orderservice.application.dto.ValidateTokenDTO;
import com.thanhnt.orderservice.application.exception.AppException;
import com.thanhnt.orderservice.application.service.*;
import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import com.thanhnt.orderservice.domain.entity.common.RolesEnum;
import com.thanhnt.orderservice.domain.entity.orders.Order;
import com.thanhnt.orderservice.domain.entity.orders.OrderItem;
import com.thanhnt.orderservice.domain.entity.vouchers.UserVoucher;
import com.thanhnt.orderservice.domain.entity.vouchers.Voucher;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFacadeImpl implements OrderFacade {

  private final ProductServiceFeign productServiceFeign;
  private final VoucherUserService voucherUserService;
  private final CacheService cacheService;
  private final UserServiceFeign userServiceFeign;
  private final StoreServiceFeign storeServiceFeign;
  private final OrderService orderService;
  private final VoucherService voucherService;

  @Value("${thanhnt.shopCacheKey}")
  private String shopKeyCachePrefix;

  @Override
  public BaseResponse<OrderDetailResponse> validateOrderRequest(ValidateOrderRequest request) {
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Validating order request for user: {}", authentication.getId());

    CustomerInfoDTO customerInfoDTO = null;

    try {
      customerInfoDTO = userServiceFeign.getCustomerInfo(authentication.getId());
    } catch (Exception e) {
      log.error("Error fetching user info: {}", e.getMessage());
      return BaseResponse.build(OrderDetailResponse.builder().valid(false).build(), false);
    }

    log.info("Validating order request: {}", request);
    List<ProductValidDTO> productValidDto =
        productServiceFeign.validateProducts(request.getOrderItems());
    if (productValidDto.isEmpty()) {
      log.error("No valid products found in the order request: {}", request);
      return BaseResponse.build(OrderDetailResponse.builder().valid(false).build(), false);
    }

    UserVoucher userVoucher = null;
    if (request.getVoucherUserId() != null)
      userVoucher = voucherUserService.getUserVoucherById(request.getVoucherUserId());

    log.info("User voucher: {}", userVoucher);

    BigDecimal totalPrice =
        productValidDto.stream()
            .map(dto -> BigDecimal.valueOf(dto.getPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    String orderCode = generateOrderCode(request.getShopId());

    if (userVoucher != null) {
      Voucher voucher = validateVoucher(userVoucher.getVoucher());

      BigDecimal totalDiscount = calculateDiscount(voucher, totalPrice);
      BigDecimal totalAmount =
          calculateTotalAmount(totalPrice, totalDiscount, request.getShippingFee());

      OrderDetailResponse orderDetailResponse =
          OrderDetailResponse.builder()
              .valid(true)
              .orderCode(orderCode)
              .totalPrice(totalPrice)
              .totalDiscount(totalDiscount)
              .totalShippingFee(request.getShippingFee())
              .totalAmount(totalAmount)
              .shippingAddress(
                  request.getShippingAddress() != null
                      ? request.getShippingAddress()
                      : customerInfoDTO.getAddress())
              .note(request.getNote())
              .customerName(
                  request.getCustomerName() != null
                      ? request.getCustomerName()
                      : customerInfoDTO.getFullName())
              .customerPhone(
                  request.getCustomerPhone() != null
                      ? request.getCustomerPhone()
                      : customerInfoDTO.getPhoneNumber())
              .voucherCode(voucher.getVoucherCode())
              .orderItems(
                  productValidDto.stream()
                      .map(
                          dto ->
                              OrderItemResponse.builder()
                                  .productVariantId(dto.getProductValidId())
                                  .productName(dto.getProductName())
                                  .imageUrl(dto.getImageUrl())
                                  .quantity(dto.getQuantity())
                                  .price(dto.getPrice())
                                  .build())
                      .toList())
              .shopId(request.getShopId())
              .build();
      log.info("Order response with voucher: {}", orderDetailResponse);
      // Store order in cache
      cacheService.storeOrder(orderCode, orderDetailResponse, 15, TimeUnit.MINUTES);
      log.info("Order stored in cache with key: {}", orderCode);
      return BaseResponse.build(orderDetailResponse, true);
    }

    BigDecimal totalAmount =
        calculateTotalAmount(totalPrice, BigDecimal.ZERO, request.getShippingFee());

    OrderDetailResponse orderDetailResponse =
        OrderDetailResponse.builder()
            .valid(true)
            .orderCode(orderCode)
            .totalPrice(totalPrice)
            .totalDiscount(BigDecimal.ZERO)
            .totalShippingFee(request.getShippingFee())
            .totalAmount(totalAmount)
            .shippingAddress(
                request.getShippingAddress() != null
                    ? request.getShippingAddress()
                    : customerInfoDTO.getAddress())
            .note(request.getNote())
            .customerName(
                request.getCustomerName() != null
                    ? request.getCustomerName()
                    : customerInfoDTO.getFullName())
            .customerPhone(
                request.getCustomerPhone() != null
                    ? request.getCustomerPhone()
                    : customerInfoDTO.getPhoneNumber())
            .orderItems(
                productValidDto.stream()
                    .map(
                        dto ->
                            OrderItemResponse.builder()
                                .productVariantId(dto.getProductValidId())
                                .productName(dto.getProductName())
                                .quantity(dto.getQuantity())
                                .imageUrl(dto.getImageUrl())
                                .price(dto.getPrice())
                                .build())
                    .toList())
            .shopId(request.getShopId())
            .build();
    log.info("Order response: {}", orderDetailResponse);
    cacheService.storeOrder(orderCode, orderDetailResponse, 15, TimeUnit.MINUTES);
    log.info("Order stored in cache with key: {}", orderCode);

    return BaseResponse.build(orderDetailResponse, true);
  }

  @Override
  public BaseResponse<Void> cancelOrder(String orderCode) {
    log.info("Cancelling order with code: {}", orderCode);
    if (cacheService.hasOrderKey(orderCode)) {
      cacheService.removeOrder(orderCode);
      log.info("Order with code {} has been cancelled and removed from cache.", orderCode);
      return BaseResponse.ok();
    }
    log.warn("Order with code {} not found in cache.", orderCode);
    throw new AppException(ErrorCode.ORDER_NOT_FOUND);
  }

  @Override
  @Transactional
  public BaseResponse<ConfirmOrderResponse> confirmOrder(
      String orderCode, PaymentType paymentType) {
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Confirming order for user: {}", authentication.getId());

    log.info("Confirming order with code: {} and payment type: {}", orderCode, paymentType);

    if (cacheService.hasOrderKey(orderCode)) {
      OrderDetailResponse orderDetailResponse = cacheService.retrieveOrder(orderCode);
      log.info("Order found in cache: {}", orderDetailResponse);

      Order order =
          Order.builder()
              .orderCode(orderCode)
              .totalPrice(orderDetailResponse.getTotalPrice())
              .discountTotal(orderDetailResponse.getTotalDiscount())
              .shippingFee(orderDetailResponse.getTotalShippingFee())
              .totalAmount(orderDetailResponse.getTotalAmount())
              .address(orderDetailResponse.getShippingAddress())
              .note(orderDetailResponse.getNote())
              .name(orderDetailResponse.getCustomerName())
              .phone(orderDetailResponse.getCustomerPhone())
              .userId(authentication.getId())
              .shopId(orderDetailResponse.getShopId())
              .paymentType(paymentType)
              .status(
                  paymentType.isOnlinePayment() ? OrderStatus.PAYMENT_PENDING : OrderStatus.PENDING)
              .build();

      List<OrderItem> orderItems =
          orderDetailResponse.getOrderItems().stream()
              .map(
                  item ->
                      OrderItem.builder()
                          .productId(item.getProductVariantId())
                          .quantity(item.getQuantity())
                          .productName(item.getProductName())
                          .imageUrl(item.getImageUrl())
                          .priceUnit(BigDecimal.valueOf(item.getPrice()))
                          .order(order)
                          .build())
              .toList();
      order.getOrderItems().addAll(orderItems);

      log.info("Creating order entity: {}", order);
      orderService.saveOrder(order);

      List<ValidateOrderItemRequest> orderItemRequests =
          orderDetailResponse.getOrderItems().stream()
              .map(
                  item ->
                      ValidateOrderItemRequest.builder()
                          .productVariantId(item.getProductVariantId())
                          .quantity(item.getQuantity())
                          .build())
              .toList();

      boolean isUpdated = productServiceFeign.updateProductQuantity(orderItemRequests);

      if (!isUpdated) {
        log.error("Failed to update product quantities for order: {}", orderCode);
      }

      if (orderDetailResponse.getVoucherCode() != null) {
        handleVoucherUsage(orderDetailResponse.getVoucherCode(), authentication.getId());
      } else {
        log.warn("No voucher found for code: {}", orderDetailResponse.getVoucherCode());
      }

      log.info("Payment confirmed for order {} with type {}", orderCode, paymentType);
      cacheService.removeOrder(orderCode);
      log.info("Order with code {} has been confirmed and removed from cache.", orderCode);
      return BaseResponse.build(
          ConfirmOrderResponse.builder().orderCode(orderCode).paymentType(paymentType).build(),
          true);
    } else {
      log.warn("Order with code {} not found in cache.", orderCode);
      throw new AppException(ErrorCode.ORDER_NOT_FOUND);
    }
  }

  @Override
  public BaseResponse<PaginationResponse<List<OrderResponse>>> getOrdersWithFilterByShop(
      OrderCriteria criteria) {
    log.info("Fetching orders with criteria: {}", criteria);
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Authenticated user ID: {}", authentication.getId());
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + authentication.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop ID from Cache: {}", authentication.getId());
      shopInfo = storeServiceFeign.checkValidShopById(authentication.getId());
      if (shopInfo == null) throw new AppException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shopID: {}", authentication.getId());
    var orderResponsePage = orderService.findByFilter(criteria, shopInfo.getId());
    log.info(
        "Found {} orders for shop ID: {}",
        orderResponsePage.getTotalElements(),
        authentication.getId());
    List<OrderResponse> orderResponses =
        orderResponsePage.getContent().stream().map(this::buildOrderResponse).toList();
    return BaseResponse.build(
        PaginationResponse.build(orderResponses, orderResponsePage, criteria.getPageSize(), true),
        true);
  }

  @Override
  public BaseResponse<OrderDetailResponse> getOrderDetailByOrderCode(String orderCode) {
    log.info("Fetching order detail for order Code: {}", orderCode);

    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long authId = authentication.getId();
    log.info("Authenticated user ID: {}", authId);

    Order order = orderService.findByOrderCode(orderCode);

    // Lấy role người dùng
    Set<String> roleNames =
        authentication.getRoles().stream().map(RolesEnum::getRoleName).collect(Collectors.toSet());

    boolean isAdmin = roleNames.contains(RolesEnum.ROLE_ADMIN.getRoleName());
    boolean isStoreOwner = roleNames.contains(RolesEnum.ROLE_STORE_OWNER.getRoleName());
    boolean isUser = roleNames.contains(RolesEnum.ROLE_USER.getRoleName());

    boolean hasPermission = false;

    if (isAdmin) {
      hasPermission = true;
    } else if (isStoreOwner && order.getShopId().equals(authId)) {
      hasPermission = true;
    } else if (isUser && order.getUserId().equals(authId)) {
      hasPermission = true;
    }

    if (!hasPermission) {
      log.warn("User {} does not have permission to view order {}", authId, orderCode);
      throw new AppException(ErrorCode.ORDER_NO_PERMISSION);
    }

    return BaseResponse.build(buildOrderDetailResponse(order), true);
  }

  @Override
  public BaseResponse<Void> approveOrder(String orderCode) {
    log.info("Approving order with code: {}", orderCode);
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + authentication.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop ID from Cache: {}", authentication.getId());
      shopInfo = storeServiceFeign.checkValidShopById(authentication.getId());
      if (shopInfo == null) throw new AppException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shopID: {}", authentication.getId());
    Order order = orderService.findByOrderCode(orderCode);
    if (!order.getShopId().equals(shopInfo.getId())) {
      log.warn("Shop ID {} does not match order shop ID {}", shopInfo.getId(), order.getShopId());
      throw new AppException(ErrorCode.ORDER_NO_PERMISSION);
    }
    order.updateStatus(OrderStatus.SHOP_CONFIRMED);
    orderService.saveOrder(order);
    log.info("Order {} has been approved by shop owner", orderCode);

    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<PaginationResponse<List<OrderResponse>>> getOrdersWithFilterByAdmin(
      OrderCriteria criteria, Long shopId) {
    log.info("Fetching orders with criteria: {} for shop ID: {}", criteria, shopId);
    var orderResponsePage = orderService.findByFilter(criteria, shopId);
    log.info("Found {} orders for shop ID: {}", orderResponsePage.getTotalElements(), shopId);
    List<OrderResponse> orderResponses =
        orderResponsePage.getContent().stream().map(this::buildOrderResponse).toList();
    return BaseResponse.build(
        PaginationResponse.build(orderResponses, orderResponsePage, criteria.getPageSize(), true),
        true);
  }

  @Override
  public BaseResponse<PaginationResponse<List<OrderCustomerResponse>>> getOrdersByCustomer(
      OrderCriteria criteria) {
    log.info("Fetching orders for customer with criteria: {}", criteria);
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Authenticated user ID: {}", authentication.getId());
    var orderResponsePage = orderService.findByFilterCustomer(criteria, authentication.getId());
    log.info(
        "Found {} orders for customer ID: {}",
        orderResponsePage.getTotalElements(),
        authentication.getId());
    List<OrderCustomerResponse> orderCustomerResponses =
        orderResponsePage.getContent().stream().map(this::buildOrderCustomerResponse).toList();
    return BaseResponse.build(
        PaginationResponse.build(
            orderCustomerResponses, orderResponsePage, criteria.getPageSize(), true),
        true);
  }

  private OrderCustomerResponse buildOrderCustomerResponse(Order order) {
    List<OrderItemResponse> orderItemResponses =
        order.getOrderItems().stream()
            .map(
                item ->
                    OrderItemResponse.builder()
                        .productVariantId(item.getProductId())
                        .quantity(item.getQuantity())
                        .imageUrl(item.getImageUrl())
                        .price(item.getPriceUnit().longValue())
                        .productName(item.getProductName())
                        .build())
            .toList();
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + order.getShopId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop ID from Cache: {}", order.getShopId());
      shopInfo = storeServiceFeign.getShopInfoById(order.getShopId());
      if (shopInfo == null) throw new AppException(ErrorCode.STORE_NOT_FOUND);
    }
    return OrderCustomerResponse.builder()
        .orderCode(order.getOrderCode())
        .id(order.getId())
        .orderItems(orderItemResponses)
        .paymentType(order.getPaymentType())
        .shopName(shopInfo.getShopName())
        .shopId(shopInfo.getId())
        .status(order.getStatus())
        .price(order.getTotalAmount())
        .build();
  }

  private OrderDetailResponse buildOrderDetailResponse(Order order) {
    List<OrderItemResponse> orderItemResponses =
        order.getOrderItems().stream()
            .map(
                item ->
                    OrderItemResponse.builder()
                        .productVariantId(item.getProductId())
                        .quantity(item.getQuantity())
                        .imageUrl(item.getImageUrl())
                        .price(item.getPriceUnit().longValue())
                        .productName(item.getProductName())
                        .build())
            .toList();

    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + order.getShopId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop ID from Cache: {}", order.getShopId());
      shopInfo = storeServiceFeign.getShopInfoById(order.getShopId());
      if (shopInfo == null) throw new AppException(ErrorCode.STORE_NOT_FOUND);
    }

    return OrderDetailResponse.builder()
        .orderCode(order.getOrderCode())
        .totalPrice(order.getTotalPrice())
        .shopName(shopInfo.getShopName())
        .totalDiscount(order.getDiscountTotal())
        .totalShippingFee(order.getShippingFee())
        .totalAmount(order.getTotalAmount())
        .shippingAddress(order.getAddress())
        .note(order.getNote())
        .customerName(order.getName())
        .customerPhone(order.getPhone())
        .orderItems(orderItemResponses)
        .voucherCode(
            order.getOrderVouchers() != null && !order.getOrderVouchers().isEmpty()
                ? order.getOrderVouchers().getFirst().getVoucher().getVoucherCode()
                : null)
        .shopId(order.getShopId())
        .orderStatus(order.getStatus())
        .paymentType(order.getPaymentType())
        .createdAt(order.getCreatedAt())
        .build();
  }

  private OrderResponse buildOrderResponse(Order order) {
    return OrderResponse.builder()
        .orderCode(order.getOrderCode())
        .id(order.getId())
        .customerName(order.getName())
        .status(order.getStatus())
        .price(order.getTotalAmount())
        .paymentType(order.getPaymentType())
        .createdAt(order.getCreatedAt())
        .build();
  }

  private void handleVoucherUsage(String orderCode, Long userId) {
    Voucher voucher = voucherService.getVoucherByCode(orderCode);
    if (voucher != null) {
      log.info("Voucher {} used for order {}", voucher.getVoucherCode(), orderCode);
      UserVoucher userVoucher =
          voucherUserService.getUserVoucherByUserIdAndVoucherId(userId, voucher.getId());
      if (userVoucher != null) {
        userVoucher.updateStatus(false);
        voucherUserService.saveUserVoucher(userVoucher);
        log.info(
            "Updating user voucher status for user {} and voucher {}", userId, voucher.getId());
      } else {
        log.warn("User voucher not found for user {} and voucher {}", userId, voucher.getId());
      }
      voucher.updateUsageLimit();
      voucherService.saveVoucher(voucher);
      log.info("Updating voucher usage limit for voucher {}", voucher.getVoucherCode());

    } else {
      log.warn("No voucher found for code: {}", orderCode);
    }
  }

  private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalPrice) {
    if (voucher.getDiscountType().isPercentage()) {
      BigDecimal discountRate =
          BigDecimal.valueOf(voucher.getDiscountValue())
              .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
      return totalPrice.multiply(discountRate);
    } else if (voucher.getDiscountType().isAmount()) {
      return BigDecimal.valueOf(voucher.getDiscountValue());
    }
    return BigDecimal.ZERO;
  }

  private BigDecimal calculateTotalAmount(
      BigDecimal totalPrice, BigDecimal totalDiscount, BigDecimal totalShippingFee) {
    return totalPrice.subtract(totalDiscount).add(totalShippingFee);
  }

  private Voucher validateVoucher(Voucher voucher) {
    if (voucher.getUsageLimit().equals(0)) {
      throw new AppException(ErrorCode.VOUCHER_EXPIRED);
    }
    if (voucher.getStartDate() != null && voucher.getStartDate() > System.currentTimeMillis()) {
      throw new AppException(ErrorCode.VOUCHER_NOT_STARTED);
    }

    if (voucher.getEndDate() != null && voucher.getEndDate() < System.currentTimeMillis()) {
      throw new AppException(ErrorCode.VOUCHER_EXPIRED);
    }
    return voucher;
  }

  private String generateOrderCode(Long shopId) {
    return "ORDER-" + shopId + "-" + System.currentTimeMillis();
  }
}

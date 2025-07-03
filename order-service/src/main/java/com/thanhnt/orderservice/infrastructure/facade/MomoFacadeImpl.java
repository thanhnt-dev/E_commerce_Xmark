package com.thanhnt.orderservice.infrastructure.facade;

import com.thanhnt.orderservice.api.facade.MomoFacade;
import com.thanhnt.orderservice.api.request.CreateMomoRequest;
import com.thanhnt.orderservice.api.response.BaseResponse;
import com.thanhnt.orderservice.api.response.CreateMomoResponse;
import com.thanhnt.orderservice.application.exception.AppException;
import com.thanhnt.orderservice.application.service.MomoServiceFeign;
import com.thanhnt.orderservice.application.service.OrderService;
import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.orders.Order;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoFacadeImpl implements MomoFacade {

  @Value("${momo.partnerCode}")
  private String partnerCode;

  @Value("${momo.accessKey}")
  private String accessKey;

  @Value("${momo.secretKey}")
  private String secretKey;

  @Value("${momo.redirectUrl}")
  private String redirectUrl;

  @Value("${momo.ipnUrl}")
  private String ipnUrl;

  @Value("${momo.requestType}")
  private String requestType;

  private final MomoServiceFeign momoServiceFeign;
  private final OrderService orderService;

  @Override
  public BaseResponse<CreateMomoResponse> createMomoPayment(CreateMomoRequest request) {
    try {
      log.info("Creating Momo payment with request: {}", request);

      Order order = orderService.findByOrderCode(request.getOrderId());
      String requestId = UUID.randomUUID().toString();
      String extraJson = "{\"message\":\"No_message\"}";
      String extraData =
          Base64.getEncoder().encodeToString(extraJson.getBytes(StandardCharsets.UTF_8));
      String orderInfo = "Payment_for_order_" + order.getOrderCode();
      Long amount = order.getTotalAmount().longValue();

      String rawSignature =
          String.format(
              "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
              accessKey,
              amount,
              extraData,
              ipnUrl,
              order.getOrderCode(),
              orderInfo,
              partnerCode,
              redirectUrl,
              requestId,
              requestType);

      log.info("Raw Momo signature data: {}", rawSignature);
      String signature = signHmacSHA256(rawSignature, secretKey);
      log.debug("Generated Momo signature: {}", signature);

      CreateMomoRequest momoRequest =
          CreateMomoRequest.builder()
              .partnerCode(partnerCode)
              .requestType(requestType)
              .ipnUrl(ipnUrl)
              .redirectUrl(redirectUrl)
              .orderId(order.getOrderCode())
              .amount(amount)
              .orderInfo(orderInfo)
              .requestId(requestId)
              .extraData(extraData)
              .lang("vi")
              .signature(signature)
              .build();

      log.info("Sending Momo payment request: {}", momoRequest.toString());
      CreateMomoResponse response = momoServiceFeign.createMomoPayment(momoRequest);

      return BaseResponse.build(response, true);

    } catch (Exception e) {
      log.error("Failed to create Momo payment: {}", e.getMessage(), e);
      return BaseResponse.build(null, false);
    }
  }

  @Override
  public BaseResponse<Void> verifyMomoPayment(Map<String, String> requestParams) {
    log.info("Verifying Momo payment with parameters: {}", requestParams);
    try {
      String orderId = requestParams.get("orderId");
      int resultCode = Integer.parseInt(requestParams.get("resultCode"));

      Order order = orderService.findByOrderCode(orderId);
      if (resultCode == 0) {
        order.updateStatus(OrderStatus.PAYMENT_SUCCESS);
        orderService.saveOrder(order);
        log.info("Momo payment successful for order: {}", orderId);
        return BaseResponse.ok();
      } else {
        String newOrderCode = generateOrderCode(order.getShopId());
        log.info("Momo payment failed for order: {}, resultCode: {}", orderId, resultCode);
        order.updateOrderCode(newOrderCode);
        order.updateStatus(OrderStatus.PAYMENT_FAILED);
        orderService.saveOrder(order);
        log.warn("Momo payment failed for order: {}, resultCode: {}", orderId, resultCode);
        throw new AppException(ErrorCode.PAYMENT_NOT_SUCCESS);
      }
    } catch (Exception e) {
      log.info("Error verifying Momo payment: {}", e.getMessage(), e);
      throw new AppException(ErrorCode.PAYMENT_FAILED);
    }
  }

  private String generateOrderCode(Long shopId) {
    return "ORDER-" + shopId + "-" + System.currentTimeMillis();
  }

  // Enhanced HMAC signature method with better error handling
  private static String signHmacSHA256(String data, String key) throws Exception {
    try {
      Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec =
          new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      hmacSHA256.init(secretKeySpec);
      byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));

      // Convert to hex string
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      log.error("Error generating HMAC-SHA256 signature", e);
      throw e;
    }
  }
}

package com.thanhnt.orderservice.infrastructure.rest.controller;

import com.thanhnt.orderservice.api.facade.MomoFacade;
import com.thanhnt.orderservice.api.request.CreateMomoRequest;
import com.thanhnt.orderservice.api.response.BaseResponse;
import com.thanhnt.orderservice.api.response.CreateMomoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/momo")
@RequiredArgsConstructor
public class MomoController {
  private final MomoFacade momoFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Create a Momo payment",
      tags = {"Momo APIs"})
  public BaseResponse<CreateMomoResponse> createMomoPayment(
      @RequestBody CreateMomoRequest request) {
    log.info("Creating Momo payment with request: {}", request);
    return this.momoFacade.createMomoPayment(request);
  }

  @GetMapping("/ipn-handler")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "IPN Handler for Momo payment",
      description = "Handles Instant Payment Notification (IPN) from Momo.",
      tags = {"Momo APIs"})
  public BaseResponse<Void> ipnHandler(@RequestParam Map<String, String> requestParams) {
    log.info("Received IPN request with parameters: {}", requestParams);
    return this.momoFacade.verifyMomoPayment(requestParams);
  }
}

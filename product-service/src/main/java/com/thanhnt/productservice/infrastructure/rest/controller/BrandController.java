package com.thanhnt.productservice.infrastructure.rest.controller;

import com.thanhnt.productservice.api.facade.BrandFacade;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.BrandResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

  private final BrandFacade brandFacade;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all Brands",
      tags = {"Brands APIs"})
  public BaseResponse<List<BrandResponse>> getAllCategories() {
    return this.brandFacade.getAllBrands();
  }
}

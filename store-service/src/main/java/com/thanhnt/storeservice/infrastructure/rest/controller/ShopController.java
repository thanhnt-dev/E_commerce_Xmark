package com.thanhnt.storeservice.infrastructure.rest.controller;

import com.thanhnt.storeservice.api.facade.ShopFacade;
import com.thanhnt.storeservice.api.request.StoreCriteria;
import com.thanhnt.storeservice.api.request.UpdateVerifyStatusStoreRequest;
import com.thanhnt.storeservice.api.request.UpsertShopRequest;
import com.thanhnt.storeservice.api.response.*;
import com.thanhnt.storeservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.storeservice.application.dto.UpdateProductShopDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class ShopController {

  private final ShopFacade shopFacade;

  @PostMapping(value = "/register")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Register new shop",
      tags = {"Store APIs"})
  public BaseResponse<Long> registerShop(@RequestBody UpsertShopRequest request) {
    return this.shopFacade.upsertShop(request);
  }

  @PostMapping(value = "/upload_identity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Upload image identity shop",
      tags = {"Store APIs"})
  public BaseResponse<Void> uploadImageIdentity(
      @RequestPart MultipartFile frontIdentity, @RequestPart MultipartFile backIdentity)
      throws IOException {
    return this.shopFacade.uploadImageIdentity(frontIdentity, backIdentity);
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get all stores by Admin",
      tags = {"Store APIs"})
  public BaseResponse<PaginationResponse<List<StoreResponse>>> getStoresByAdmin(
      @Nullable StoreCriteria criteria) {

    return this.shopFacade.getStoresByAdminWithFilter(criteria);
  }

  @GetMapping("/detail/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get details stores by Admin",
      tags = {"Store APIs"})
  public BaseResponse<StoreDetailsResponse> getStoreDetails(@PathVariable Long id) {
    return this.shopFacade.getStoreDetailsByAdmin(id);
  }

  @PutMapping("/verify-status/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Update store status by Admin",
      tags = {"Store APIs"})
  public BaseResponse<Void> updateStoreApprovalStatus(
      @PathVariable Long id, @RequestBody UpdateVerifyStatusStoreRequest request) {
    return this.shopFacade.updateStoreApprovalStatus(id, request);
  }

  @GetMapping("/detail")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get details stores by Owner",
      tags = {"Store APIs"})
  public BaseResponse<StoreDetailsResponse> getStoreDetailByOwner() {
    return this.shopFacade.getStoreDetailsByOwner();
  }

  @PutMapping
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Update info stores by Owner",
      tags = {"Store APIs"})
  public BaseResponse<Long> updateStoreApprovalStatus(
      @RequestBody @Validated UpsertShopRequest request) {
    return this.shopFacade.upsertShop(request);
  }

  @PostMapping("/upload_avatar")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Upload avatar stores by Owner",
      tags = {"Store APIs"})
  public BaseResponse<String> uploadAvatar(@RequestPart MultipartFile avatar) throws IOException {
    return this.shopFacade.uploadAvatar(avatar);
  }

  @GetMapping("/detail/{storeId}/customer")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get Info stores by Customer",
      tags = {"Store APIs"})
  public BaseResponse<ShopInfoResponse> getShopInfoById(@PathVariable Long storeId) {
    return this.shopFacade.getShopInfoById(storeId);
  }

  @Hidden
  @GetMapping(path = "/valid-store/{id}", headers = "API_SECRET_HEADER=secret1403")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Check valid shop by Id",
      tags = {"Store APIs"})
  public ResponseEntity<ShopSnapshotDTO> checkValidShopById(@PathVariable Long id) {
    return this.shopFacade.checkValidShopById(id);
  }

  @Hidden
  @GetMapping(path = "/info/{shopId}", headers = "API_SECRET_HEADER=secret1403")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get Info shop by Id",
      tags = {"Store APIs"})
  public ResponseEntity<ShopSnapshotDTO> getInfoShopByShopId(@PathVariable Long shopId) {
    return this.shopFacade.getInfoShopByShopId(shopId);
  }

  @Hidden
  @PostMapping(path = "/add_product_shop")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Add product to shop",
      tags = {"Store APIs"})
  public ResponseEntity<Void> addProductToShop(
      @RequestBody UpdateProductShopDTO updateProductShopDTO) {
    return this.shopFacade.addProductId(updateProductShopDTO);
  }
}

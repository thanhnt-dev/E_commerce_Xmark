package com.thanhnt.userservice.infrastructure.rest.controller;

import com.thanhnt.userservice.api.facade.UserFacade;
import com.thanhnt.userservice.api.request.RoleUpdateRequest;
import com.thanhnt.userservice.api.request.UpdateProfileRequest;
import com.thanhnt.userservice.api.request.UpsertAddressRequest;
import com.thanhnt.userservice.api.response.AddressResponse;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.UserProfileResponse;
import com.thanhnt.userservice.application.dto.CustomerInfoDTO;
import com.thanhnt.userservice.application.dto.StoreOwnerDetailDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserFacade userFacade;

  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get user profile",
      tags = {"User APIs"})
  public BaseResponse<UserProfileResponse> getProfile() {
    return this.userFacade.getProfile();
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update user profile",
      tags = {"User APIs"})
  public BaseResponse<UserProfileResponse> updateProfile(
      @RequestBody @Nullable UpdateProfileRequest request) {
    return this.userFacade.updateProfile(request);
  }

  @PutMapping(value = "/update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      tags = {"User APIs"},
      summary = "Update avatar user")
  public BaseResponse<String> updateAvatar(@RequestPart MultipartFile file) {
    return this.userFacade.updateAvatar(file);
  }

  @PostMapping(value = "/address/create")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      tags = {"User APIs"},
      summary = "Create new Address")
  public BaseResponse<List<AddressResponse>> createAddress(
      @RequestBody @Validated UpsertAddressRequest request) {
    return this.userFacade.upsertAddress(request);
  }

  @PutMapping(value = "/address/update")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      tags = {"User APIs"},
      summary = "Update Address")
  public BaseResponse<List<AddressResponse>> updateAddress(
      @RequestBody @Validated UpsertAddressRequest request) {
    return this.userFacade.upsertAddress(request);
  }

  @DeleteMapping(value = "/address/delete/{id}")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      tags = {"User APIs"},
      summary = "Delete Address By Id")
  public BaseResponse<Void> deleteAddress(@PathVariable Long id) {
    return this.userFacade.deleteAddress(id);
  }

  @Hidden
  @GetMapping(value = "/store-owner/{id}", headers = "API_SECRET_HEADER=secret1403")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get store owner detail by id",
      tags = {"User APIs"})
  public ResponseEntity<StoreOwnerDetailDTO> getStoreOwnerDetail(@PathVariable Long id) {
    return this.userFacade.getStoreOwnerDetail(id);
  }

  @Hidden
  @PostMapping(value = "/permission", headers = "API_SECRET_HEADER=secret1403")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Update Role Store Owner By Admin",
      tags = {"User APIs"})
  public ResponseEntity<Boolean> updateStoreOwnerRole(@RequestBody RoleUpdateRequest request) {
    return this.userFacade.updateRoleByAdmin(request);
  }

  @Hidden
  @GetMapping(value = "/customer-info/{id}", headers = "API_SECRET_HEADER=secret1403")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Get customer info by id",
      tags = {"User APIs"})
  public ResponseEntity<CustomerInfoDTO> getCustomerInfo(@PathVariable Long id) {
    return this.userFacade.getCustomerInfo(id);
  }
}

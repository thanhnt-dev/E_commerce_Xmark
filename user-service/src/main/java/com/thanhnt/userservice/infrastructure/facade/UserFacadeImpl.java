package com.thanhnt.userservice.infrastructure.facade;

import com.thanhnt.userservice.api.facade.UserFacade;
import com.thanhnt.userservice.api.request.RoleUpdateRequest;
import com.thanhnt.userservice.api.request.UpdateProfileRequest;
import com.thanhnt.userservice.api.request.UpsertAddressRequest;
import com.thanhnt.userservice.api.response.AddressResponse;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.UserProfileResponse;
import com.thanhnt.userservice.application.dto.CustomerInfoDTO;
import com.thanhnt.userservice.application.dto.StoreOwnerDetailDTO;
import com.thanhnt.userservice.application.dto.UserSnapshotDTO;
import com.thanhnt.userservice.application.exception.AddressException;
import com.thanhnt.userservice.application.exception.UpdateUserException;
import com.thanhnt.userservice.application.service.*;
import com.thanhnt.userservice.domain.entity.address.Address;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.users.Users;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {

  private final AccountService accountService;
  private final CloudinaryService cloudinaryService;
  private final AddressService addressService;
  private final UserService userService;
  private final CacheService cacheService;
  private final RoleService roleService;

  @Value("${thanhnt.userCacheKey}")
  private String userKeyCachePrefix;

  @Override
  public BaseResponse<UserProfileResponse> getProfile() {

    var principal =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Fetching profile for user: {}", principal.getEmail());
    var user = accountService.findByEmail(principal.getEmail());

    List<AddressResponse> addresses = getAddressResponses(user.getAddresses());
    String avatar =
        user.getAvatar() == null ? "" : this.cloudinaryService.getImageUrl(user.getAvatar());
    return BaseResponse.build(
        UserProfileResponse.builder()
            .email(user.getEmail())
            .phone(user.getPhone())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .avatar(avatar)
            .addresses(addresses)
            .dateOfBirth(user.getDateOfBirth())
            .isEnableTwoFactor(user.isEnableTwoFactor())
            .gender(user.getGender().toString())
            .build(),
        true);
  }

  @Override
  @Transactional
  public BaseResponse<UserProfileResponse> updateProfile(UpdateProfileRequest request) {
    log.info("Updating profile for user: {}", request.toString());
    var principal =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var user = accountService.findByEmail(principal.getEmail());
    log.info("Found user: {}", user.getEmail());
    boolean isPhoneChanged = !user.getPhone().equals(request.getPhone());
    if (isPhoneChanged) {
      boolean isExistsByPhoneNumber = accountService.existByPhone(request.getPhone());
      if (isExistsByPhoneNumber) throw new UpdateUserException(ErrorCode.PHONE_EXIST);
    }

    user.updateProfile(
        request.getFirstName(),
        request.getLastName(),
        request.getGender(),
        request.getPhone(),
        request.getDateOfBirth(),
        request.isEnableTwoFactor());
    accountService.updateUser(user);
    log.info("User profile updated successfully: {}", user.getEmail());

    List<AddressResponse> addresses = getAddressResponses(user.getAddresses());
    String avatar =
        user.getAvatar() == null ? "" : this.cloudinaryService.getImageUrl(user.getAvatar());

    boolean hasUserKey = cacheService.hasUserKey(userKeyCachePrefix + user.getId());
    if (hasUserKey) {
      cacheService.removeUser(userKeyCachePrefix + user.getId());
    }
    cacheService.storeUser(
        userKeyCachePrefix + user.getId(),
        UserSnapshotDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .phone(user.getPhone())
            .fullName(user.getFirstName() + ' ' + user.getLastName())
            .avatar(avatar)
            .build());
    log.info("User profile cached successfully for user: {}", user.getEmail());

    return BaseResponse.build(
        UserProfileResponse.builder()
            .email(user.getEmail())
            .phone(user.getPhone())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .avatar(avatar)
            .addresses(addresses)
            .dateOfBirth(user.getDateOfBirth())
            .isEnableTwoFactor(user.isEnableTwoFactor())
            .gender(user.getGender().toString())
            .build(),
        true);
  }

  private List<AddressResponse> getAddressResponses(List<Address> addresses) {
    return addresses.stream()
        .filter(Address::isActive)
        .map(
            address ->
                AddressResponse.builder()
                    .id(address.getId())
                    .name(address.getName())
                    .phone(address.getPhone())
                    .type(address.getType().toString())
                    .detail(address.getDetail())
                    .ward(address.getWardName())
                    .district(address.getDistrictName())
                    .province(address.getProvinceName())
                    .build())
        .toList();
  }

  @Override
  @SneakyThrows
  public BaseResponse<String> updateAvatar(MultipartFile file) {
    var principal =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Updating avatar for user: {}", principal.getEmail());
    var user = accountService.findByEmail(principal.getEmail());
    if (user.getPublicId() != null) {
      this.cloudinaryService.deleteImage(user.getPublicId());
    }
    Map<String, String> accessKey = this.cloudinaryService.uploadImage(file.getBytes());
    user.updateAvatar(accessKey.get("asset_id"), accessKey.get("public_id"));
    accountService.updateUser(user);
    log.info("Avatar updated successfully for user: {}", user.getEmail());
    return BaseResponse.build(this.cloudinaryService.getImageUrl(accessKey.get("asset_id")), true);
  }

  @Override
  @Transactional
  public BaseResponse<List<AddressResponse>> upsertAddress(UpsertAddressRequest request) {
    log.info("Upserting address for user: {}", request.toString());
    var principal =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var user = accountService.findByEmail(principal.getEmail());
    log.info("Found user: {}", user.getEmail());
    Address address = addressService.findById(request.getId());
    if (address != null) {
      address.updateAddress(
          request.getName(),
          request.getPhone(),
          request.getAddressType(),
          request.getDetail(),
          request.getWard(),
          request.getDistrict(),
          request.getProvince());
      addressService.updateAddress(address);
      log.info("Address updated successfully for user: {}", user.getEmail());
    } else {
      address =
          Address.builder()
              .name(request.getName())
              .phone(request.getPhone())
              .type(request.getAddressType())
              .detail(request.getDetail())
              .wardName(request.getWard())
              .districtName(request.getDistrict())
              .provinceName(request.getProvince())
              .users(user)
              .build();
      user.addAddress(address);
      accountService.updateUser(user);
      log.info("Address created successfully for user: {}", user.getEmail());
    }
    var userAddress = accountService.findByEmail(principal.getEmail()).getAddresses();
    List<AddressResponse> addresses = getAddressResponses(userAddress);
    return BaseResponse.build(addresses, true);
  }

  @Override
  @Transactional
  public BaseResponse<Void> deleteAddress(Long addressId) {
    log.info("Deleting address with ID: {}", addressId);
    var address = addressService.findById(addressId);
    if (address == null) {
      throw new AddressException(ErrorCode.ADDRESS_NOT_FOUND);
    }
    address.deactivate();
    addressService.updateAddress(address);
    log.info("Address with ID: {} deleted successfully", addressId);
    return BaseResponse.ok();
  }

  @Override
  public ResponseEntity<StoreOwnerDetailDTO> getStoreOwnerDetail(Long id) {
    log.info("Fetching store owner detail for ID: {}", id);
    var storeOwner = userService.getOwnerStoreById(id);

    return ResponseEntity.ok(
        StoreOwnerDetailDTO.builder()
            .storeOwnerId(storeOwner.getId())
            .storeOwnerName(storeOwner.getFirstName() + storeOwner.getLastName())
            .storeOwnerEmail(storeOwner.getEmail())
            .storeOwnerPhone(storeOwner.getPhone())
            .storeOwnerAvatar(
                storeOwner.getAvatar() == null
                    ? ""
                    : this.cloudinaryService.getImageUrl(storeOwner.getAvatar()))
            .dateOfBirth(storeOwner.getDateOfBirth())
            .createdAt(storeOwner.getCreatedAt())
            .build());
  }

  @Override
  @Transactional
  public ResponseEntity<Boolean> updateRoleByAdmin(RoleUpdateRequest request) {
    try {
      log.info("Updating role for user ID: {}", request.getId());
      Users user = userService.findById(request.getId());
      Roles role = roleService.findRole(request.getRole());
      user.addRole(role);
      user.updateAt();
      userService.saveUser(user);
      log.info("Role updated successfully for user ID: {}", request.getId());
      return ResponseEntity.ok(true);
    } catch (Exception e) {
      log.error("Failed to update role for userId {}: {}", request.getId(), e.getMessage());
      return ResponseEntity.ok(false);
    }
  }

  @Override
  public ResponseEntity<CustomerInfoDTO> getCustomerInfo(Long id) {
    log.info("Fetching customer info for ID: {}", id);
    try {
      Users user = userService.findById(id);
      if (user == null) {
        log.error("User not found with ID: {}", id);
        return ResponseEntity.notFound().build();
      }
      CustomerInfoDTO customerInfo =
          CustomerInfoDTO.builder()
              .id(user.getId())
              .email(user.getEmail())
              .phoneNumber(user.getPhone())
              .fullName(user.getFirstName() + " " + user.getLastName())
              .address(
                  user.getAddresses().stream()
                      .filter(Address::isActive)
                      .map(
                          address ->
                              address.getDetail()
                                  + ", "
                                  + address.getWardName()
                                  + ", "
                                  + address.getDistrictName()
                                  + ", "
                                  + address.getProvinceName())
                      .findFirst()
                      .orElse("No address available"))
              .build();
      log.info("Customer info fetched successfully for ID: {}", id);
      return ResponseEntity.ok(customerInfo);

    } catch (Exception e) {
      log.error("Error fetching customer info for ID {}: {}", id, e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }
}

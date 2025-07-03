package com.thanhnt.storeservice.infrastructure.facade;

import com.thanhnt.storeservice.api.facade.ShopFacade;
import com.thanhnt.storeservice.api.request.RoleUpdateRequest;
import com.thanhnt.storeservice.api.request.StoreCriteria;
import com.thanhnt.storeservice.api.request.UpdateVerifyStatusStoreRequest;
import com.thanhnt.storeservice.api.request.UpsertShopRequest;
import com.thanhnt.storeservice.api.response.*;
import com.thanhnt.storeservice.application.dto.*;
import com.thanhnt.storeservice.application.exception.ShopException;
import com.thanhnt.storeservice.application.service.*;
import com.thanhnt.storeservice.domain.entity.common.ErrorCode;
import com.thanhnt.storeservice.domain.entity.common.RolesEnum;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import com.thanhnt.storeservice.domain.entity.shopinfoasset.ShopInfoAssets;
import com.thanhnt.storeservice.domain.entity.shopproduct.ShopProducts;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopFacadeImpl implements ShopFacade {
  private final ShopService shopService;
  private final UserServiceFeign userServiceFeign;
  private final CloudinaryService cloudinaryService;
  private final StoreMailQueueProducer storeMailQueueProducer;
  private final CacheService cacheService;
  private final ShopProductService shopProductService;

  @Value("${thanhnt.shopCacheKey}")
  private String shopKeyCachePrefix;

  @Override
  @Transactional
  public BaseResponse<Long> upsertShop(UpsertShopRequest request) {
    log.info("Upsert shop request: {}", request.toString());
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var existingShop = shopService.findByOwnerId(user.getId());
    log.info("Existing shop for user {}: {}", user.getId(), existingShop);
    boolean isIndividual = request.getBusinessType().isIndividual();

    if (!isIndividual) {
      log.info("Shop is not individual, checking required fields");
      if (request.getCategory() == null) throw new ShopException(ErrorCode.CATEGORY_REQUIRED);
      if (shopService.existsByEmail(request.getEmail()))
        throw new ShopException(ErrorCode.EMAIL_ALREADY_REGISTERED);
      if (shopService.existByPhone(request.getPhone()))
        throw new ShopException(ErrorCode.PHONE_ALREADY_REGISTERED);
    }

    if (existingShop != null) {
      log.info("Shop already exists for user: {}", user.getId());
      if (existingShop.getVerificationStatus().isVerified()) {
        throw new ShopException(ErrorCode.SHOP_VERIFIED);
      }

      existingShop.updateInfoShop(
          request.getShopName(),
          request.getDescription(),
          request.getLocation(),
          request.getIdentityNumber(),
          request.getOwnerName(),
          isIndividual ? existingShop.getEmail() : request.getEmail(),
          isIndividual ? existingShop.getPhone() : request.getPhone(),
          request.getTaxCode(),
          request.getBusinessType(),
          isIndividual ? existingShop.getCategory() : request.getCategory());

      shopService.saveShop(existingShop);
      log.info("Updated existing shop with ID: {}", existingShop.getId());
      return BaseResponse.build(existingShop.getId(), true);
    }

    Shop shop =
        Shop.builder()
            .name(isIndividual ? user.getEmail() : request.getShopName())
            .identityNumber(request.getIdentityNumber())
            .location(request.getLocation())
            .ownerName(request.getOwnerName())
            .description(isIndividual ? "-" : request.getDescription())
            .businessType(request.getBusinessType())
            .taxCode(request.getTaxCode())
            .owner(user.getId())
            .email(isIndividual ? user.getEmail() : request.getEmail())
            .phone(isIndividual ? user.getPhone() : request.getPhone())
            .category(isIndividual ? null : request.getCategory())
            .verificationStatus(VerificationStatus.PENDING)
            .build();
    shop = shopService.saveShop(shop);
    return BaseResponse.build(shop.getId(), true);
  }

  @Override
  @Transactional
  public BaseResponse<Void> uploadImageIdentity(
      MultipartFile frontIdentity, MultipartFile backIdentity) throws IOException {
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Uploading identity images for user: {}", user.getId());
    var shop = shopService.findByOwnerId(user.getId());
    Map<String, String> frontIdentityMediaKey =
        cloudinaryService.uploadImage(frontIdentity.getBytes());
    Map<String, String> backIdentityMediaKey =
        cloudinaryService.uploadImage(backIdentity.getBytes());
    if (shop.getShopInfoAssets() != null) {
      for (var mediaKey : shop.getShopInfoAssets()) {
        cloudinaryService.deleteImage(mediaKey.getPublicId());
      }
      shop.getShopInfoAssets().clear();
    }
    List<Map<String, String>> mediaKeys = List.of(frontIdentityMediaKey, backIdentityMediaKey);
    List<ShopInfoAssets> newAssets =
        mediaKeys.stream()
            .map(
                mediaKey ->
                    ShopInfoAssets.builder()
                        .mediaKey(mediaKey.get("asset_id"))
                        .publicId(mediaKey.get("public_id"))
                        .shop(shop)
                        .build())
            .toList();
    shop.getShopInfoAssets().addAll(newAssets);
    shopService.saveShop(shop);
    log.info("Uploaded identity images for shop ID: {}", shop.getId());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<PaginationResponse<List<StoreResponse>>> getStoresByAdminWithFilter(
      StoreCriteria storeCriteria) {
    log.info("Getting stores by admin with filter: {}", storeCriteria.toString());
    var result = shopService.findByFilter(storeCriteria);
    List<StoreResponse> storeResponses =
        result.getContent().stream().map(this::buildStoreResponse).toList();
    log.info("Found {} stores matching criteria", storeResponses.size());
    return BaseResponse.build(
        PaginationResponse.build(storeResponses, result, storeCriteria.getCurrentPage(), true),
        true);
  }

  @Override
  public BaseResponse<StoreDetailsResponse> getStoreDetailsByAdmin(Long id) {
    var admin =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Getting store details by admin for store ID: {}", id);
    var shop = shopService.findById(id);
    var owner = userServiceFeign.getOwnerById(shop.getOwner());
    log.info("Found store: {}", shop.getId());
    return BaseResponse.build(buildStoreDetailsResponse(shop, admin, owner), true);
  }

  @Override
  @Transactional
  public BaseResponse<Void> updateStoreApprovalStatus(
      Long id, UpdateVerifyStatusStoreRequest request) {
    var admin =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info(
        "Admin {} updating store approval status for store ID: {} with status: {}",
        admin.getId(),
        id,
        request.getVerificationStatus());
    var shop = shopService.findById(id);
    shop.updateVerifyStatus(request.getVerificationStatus(), request.getReason(), admin.getId());
    log.info("Updated store verification status for store ID: {}", shop.getId());
    shopService.saveShop(shop);

    updateShopCache(shop);

    storeMailQueueProducer.sendStoreRegistrationNotification(
        StoreRegistrationNotificationDTO.builder()
            .receiverMail(shop.getEmail())
            .ownerName(shop.getOwnerName())
            .reason(request.getReason())
            .verificationStatus(request.getVerificationStatus())
            .build());
    if (request.getVerificationStatus().isVerified()) {
      boolean isSuccess =
          userServiceFeign.updateUserRole(
              RoleUpdateRequest.builder()
                  .role(RolesEnum.ROLE_STORE_OWNER)
                  .id(shop.getOwner())
                  .build());
      log.info("Updated user role for store owner: {}, success: {}", shop.getOwner(), isSuccess);
      if (!isSuccess) {
        throw new ShopException(ErrorCode.UPDATE_USER_ROLE_FAILED);
      }
    }
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<StoreDetailsResponse> getStoreDetailsByOwner() {
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Getting store details by owner for user ID: {}", user.getId());
    var owner = userServiceFeign.getOwnerById(user.getId());
    if (owner == null) {
      log.error("Owner not found for user ID: {}", user.getId());
    }
    var shop = shopService.findByOwnerId(user.getId());
    if (shop == null) {
      log.error("Shop not found for owner ID: {}", user.getId());
    }
    String frontIdentityNumber = null, backIdentityNumber = null;
    if (CollectionUtils.isNotEmpty(shop.getShopInfoAssets())) {
      String frontIdentityNumberKey = shop.getShopInfoAssets().get(0).getMediaKey();
      frontIdentityNumber = this.cloudinaryService.getImageUrl(frontIdentityNumberKey);

      String backIdentityNumberKey = shop.getShopInfoAssets().get(1).getMediaKey();
      backIdentityNumber = this.cloudinaryService.getImageUrl(backIdentityNumberKey);
    }

    List<StoreRequestResponse> requestResponses =
        shop.getShopRequests().stream()
            .map(
                x ->
                    StoreRequestResponse.builder()
                        .id(x.getId())
                        .reason(x.getReason())
                        .reviewAt(x.getCreatedAt())
                        .build())
            .toList();
    return BaseResponse.build(
        StoreDetailsResponse.builder()
            .id(shop.getId())
            .name(shop.getName())
            .verificationStatus(shop.getVerificationStatus().toString())
            .businessType(shop.getBusinessType().toString())
            .createdDate(shop.getCreatedAt())
            .phone(shop.getPhone())
            .email(shop.getEmail())
            .address(shop.getLocation())
            .description(shop.getDescription())
            .category(shop.getBusinessType().isBusinessType() ? shop.getCategory() : null)
            .identityNumber(shop.getIdentityNumber())
            .frontIdentityNumber(frontIdentityNumber)
            .backIdentityNumber(backIdentityNumber)
            .nameOwner(shop.getOwnerName())
            .emailOwner(owner.getStoreOwnerEmail())
            .phoneOwner(owner.getStoreOwnerPhone())
            .storeRequests(requestResponses)
            .build(),
        true);
  }

  @Override
  public BaseResponse<String> uploadAvatar(MultipartFile avatar) throws IOException {
    log.info("Uploading avatar for shop");
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Current user ID: {}", user.getId());
    var shop = shopService.findByOwnerId(user.getId());
    if (shop == null) {
      log.error("Shop not found for user ID: {}", user.getId());
    }
    if (shop.getPublicId() != null) {
      cloudinaryService.deleteImage(shop.getPublicId());
    }
    Map<String, String> avatarMediaKey = cloudinaryService.uploadImage(avatar.getBytes());
    shop.updateAvatar(avatarMediaKey.get("asset_id"), avatarMediaKey.get("public_id"));
    shopService.saveShop(shop);
    String avatarUrl = cloudinaryService.getImageUrl(avatarMediaKey.get("asset_id"));

    updateShopCache(shop);
    log.info("Avatar uploaded successfully for shop ID: {}", shop.getId());
    return BaseResponse.build(avatarUrl, true);
  }

  @Override
  public BaseResponse<ShopInfoResponse> getShopInfoById(Long shopId) {
    log.info("Getting shop info by ID: {}", shopId);
    var shop = shopService.findById(shopId);
    if (shop == null) {
      log.error("Shop not found for ID: {}", shopId);
    }
    String avatarUrl = null;
    if (shop.getAvatar() != null) {
      avatarUrl = cloudinaryService.getImageUrl(shop.getAvatar());
    }
    return BaseResponse.build(
        ShopInfoResponse.builder()
            .shopId(shop.getId())
            .shopName(shop.getName())
            .avatarUrl(avatarUrl)
            .isOnline(shop.isActive())
            .lastOnline(0L)
            .productsCount(shop.getShopProducts().size())
            .following(shop.getShopSubscriptions().size())
            .followers(shop.getShopFollowers().size())
            .responseRate(0)
            .responseTime("0")
            .rating(0)
            .joinedSince(shop.getCreatedAt())
            .businessType(shop.getBusinessType())
            .build(),
        true);
  }

  @Override
  public ResponseEntity<ShopSnapshotDTO> checkValidShopById(Long id) {
    log.info("Checking valid shop by ID: {}", id);
    var shop = shopService.findByOwnerId(id);
    if (shop == null) {
      log.error("Shop not found for ID: {}", id);
    }
    if (shop != null && shop.getVerificationStatus().isVerified() && shop.isActive()) {
      return ResponseEntity.ok(
          ShopSnapshotDTO.builder()
              .businessType(shop.getBusinessType())
              .avatar(shop.getAvatar())
              .shopName(shop.getName())
              .email(shop.getEmail())
              .phone(shop.getPhone())
              .id(shop.getId())
              .build());
    }
    return null;
  }

  @Override
  public ResponseEntity<ShopSnapshotDTO> getInfoShopByShopId(Long shopId) {
    log.info("Checking valid shop by ID: {}", shopId);
    var shop = shopService.findById(shopId);
    if (shop == null) {
      log.error("Shop not found for ID: {}", shopId);
    }
    if (shop != null && shop.getVerificationStatus().isVerified() && shop.isActive()) {
      return ResponseEntity.ok(
          ShopSnapshotDTO.builder()
              .businessType(shop.getBusinessType())
              .avatar(shop.getAvatar())
              .shopName(shop.getName())
              .email(shop.getEmail())
              .phone(shop.getPhone())
              .id(shop.getId())
              .build());
    }
    return null;
  }

  @Override
  public ResponseEntity<Void> addProductId(UpdateProductShopDTO updateProductShopDTO) {
    log.info("Adding product ID to shop: {}", updateProductShopDTO.toString());
    var shop = shopService.findByOwnerId(updateProductShopDTO.getShopId());
    if (shop == null) {
      log.error("Shop not found for ID: {}", updateProductShopDTO.getShopId());
    }
    shopProductService.saveShopProduct(
        ShopProducts.builder().productId(updateProductShopDTO.getProductId()).shop(shop).build());
    return ResponseEntity.ok().build();
  }

  private void updateShopCache(Shop shop) {
    log.info("Updating shop cache for shop ID: {}", shop.getId());
    if (shop.getVerificationStatus().isVerified()) {
      log.info("Shop is verified, updating cache");
      if (cacheService.hasShopKey(shopKeyCachePrefix + shop.getOwner())) {
        cacheService.removeShop(shopKeyCachePrefix + shop.getOwner());
      }
      log.info("Storing shop in cache with key: {}", shopKeyCachePrefix + shop.getOwner());
      cacheService.storeShop(
          shopKeyCachePrefix + shop.getOwner(),
          ShopSnapshotDTO.builder()
              .businessType(shop.getBusinessType())
              .avatar(shop.getAvatar())
              .shopName(shop.getName())
              .email(shop.getEmail())
              .phone(shop.getPhone())
              .id(shop.getId())
              .build());
    }
  }

  private StoreResponse buildStoreResponse(Shop shop) {
    return StoreResponse.builder()
        .id(shop.getId())
        .name(shop.getName())
        .verificationStatus(shop.getVerificationStatus())
        .businessType(shop.getBusinessType())
        .createdDate(shop.getCreatedAt())
        .build();
  }

  private StoreDetailsResponse buildStoreDetailsResponse(
      Shop shop, ValidateTokenDTO admin, StoreOwnerDetailDTO owner) {
    String frontIdentityNumber = null, backIdentityNumber = null;
    if (CollectionUtils.isNotEmpty(shop.getShopInfoAssets())) {
      String frontIdentityNumberKey = shop.getShopInfoAssets().get(0).getMediaKey();
      frontIdentityNumber = this.cloudinaryService.getImageUrl(frontIdentityNumberKey);

      String backIdentityNumberKey = shop.getShopInfoAssets().get(1).getMediaKey();
      backIdentityNumber = this.cloudinaryService.getImageUrl(backIdentityNumberKey);
    }

    List<StoreRequestResponse> requestResponses =
        shop.getShopRequests().stream()
            .map(
                x ->
                    StoreRequestResponse.builder()
                        .id(x.getId())
                        .adminEmail(admin.getEmail())
                        .reason(x.getReason())
                        .reviewAt(x.getCreatedAt())
                        .build())
            .toList();

    return StoreDetailsResponse.builder()
        .id(shop.getId())
        .name(shop.getName())
        .verificationStatus(shop.getVerificationStatus().toString())
        .businessType(shop.getBusinessType().toString())
        .createdDate(shop.getCreatedAt())
        .phone(shop.getPhone())
        .email(shop.getEmail())
        .address(shop.getLocation())
        .description(shop.getDescription())
        .category(shop.getBusinessType().isBusinessType() ? shop.getCategory() : null)
        .identityNumber(shop.getIdentityNumber())
        .frontIdentityNumber(frontIdentityNumber)
        .backIdentityNumber(backIdentityNumber)
        .nameOwner(shop.getOwnerName())
        .emailOwner(owner.getStoreOwnerEmail())
        .phoneOwner(owner.getStoreOwnerPhone())
        .storeRequests(requestResponses)
        .build();
  }
}

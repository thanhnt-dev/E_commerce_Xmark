package com.thanhnt.productservice.infrastructure.facade;

import com.thanhnt.productservice.api.facade.ProductFacade;
import com.thanhnt.productservice.api.request.*;
import com.thanhnt.productservice.api.response.*;
import com.thanhnt.productservice.application.dto.*;
import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.application.exception.StoreException;
import com.thanhnt.productservice.application.service.*;
import com.thanhnt.productservice.domain.entity.brand.Brand;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.common.NotificationType;
import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.product.Product;
import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;
import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFacadeImpl implements ProductFacade {
  private final CacheService cacheService;
  private final BrandService brandService;
  private final ProductService productService;
  private final ProductVariantService productVariantService;
  private final ProductAssetService productAssetService;
  private final SubCategoryService subCategoryService;
  private final CloudinaryService cloudinaryService;
  private final ProductElasticsearch productElasticsearch;
  private final StoreServiceFeign storeServiceFeign;
  private final ProductQueueProducer productQueueProducer;
  private final NotificationProducer notificationProducer;

  @Value("${thanhnt.shopCacheKey}")
  private String shopKeyCachePrefix;

  @Value("${thanhnt.defaultImageProduct}")
  private String imageDefaultProduct;

  @Value("${thanhnt.adminId}")
  private Long adminId;

  @Value("${thanhnt.adminName}")
  private String adminName;

  @Override
  @Transactional
  public BaseResponse<Long> upsertProduct(UpsertProductRequest request) {
    log.info("Upserting product with request: {}", request.toString());
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Shop owner ID: {}", shopOwner.getId());
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    Brand brand = brandService.findById(request.getBrandId());
    SubCategory subCategory = subCategoryService.findById(request.getSubcategory());

    if (request.getRequestType().isCreate()) {
      String productCode = generateProductCode();

      Product productCreate =
          Product.builder()
              .productSku(productCode)
              .productName(request.getName())
              .story(request.getStory())
              .origin(request.getOrigin())
              .productDetails(request.getProductDetails())
              .brand(brand)
              .subCategory(subCategory)
              .saleStatus(request.getSaleStatus())
              .shopId(shopInfo.getId())
              .productApprovalStatus(
                  shopInfo.getBusinessType().isBusinessType()
                      ? ProductApprovalStatus.APPROVED
                      : ProductApprovalStatus.PENDING)
              .build();
      productCreate.updateStatus(shopInfo.getBusinessType().isBusinessType());

      List<ProductVariant> productVariants =
          request.getVariantRequestList().stream()
              .map(
                  variant ->
                      ProductVariant.builder()
                          .productSize(variant.getProductSize())
                          .quantity(variant.getQuantity())
                          .originalPrice(variant.getOriginalPrice())
                          .resalePrice(variant.getResalePrice())
                          .condition(variant.getProductCondition())
                          .product(productCreate)
                          .build())
              .toList();

      productCreate.getProductVariants().addAll(productVariants);
      Product result = productService.save(productCreate);
      log.info("Product created with ID: {}", result.getId());
      ProductElasticsearchDTO productElasticsearchDTO =
          convertToElasticsearchDTO(result, shopInfo.getShopName());

      storeServiceFeign.updateProductShop(
          UpdateProductShopDTO.builder()
              .productId(result.getId())
              .shopId(shopOwner.getId())
              .build());

      productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
      log.info("Product Create synced to Elasticsearch with ID: {}", result.getId());

      String content =
          String.format(
              "New product %s has been created by %s. Please check it out.",
              productCreate.getProductName(), shopInfo.getShopName());
      notificationProducer.sendNotification(
          NotificationDTO.builder()
              .content(content)
              .createdAt(productCreate.getCreatedAt())
              .type(NotificationType.PRODUCT)
              .senderId(shopOwner.getId())
              .senderName(shopInfo.getShopName())
              .receiverId(adminId)
              .receiverName(adminName)
              .build());
      return BaseResponse.build(result.getId(), true);
    }

    Product productUpdate = productService.findById(request.getId());
    log.info("Updating product with ID: {}", productUpdate.getId());
    productUpdate.updateProduct(
        request.getName(),
        request.getDescription(),
        request.getStory(),
        request.getOrigin(),
        request.getProductDetails(),
        request.getSaleStatus(),
        brand,
        subCategory,
        shopInfo.getId(),
        request.getApprovalStatus(),
        request.getProductApprovalBy(),
        request.getReasonReject());

    productUpdate.updateAt();
    productService.save(productUpdate);
    log.info("Product updated with ID: {}", productUpdate.getId());
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(productUpdate, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info("Product Update synced to Elasticsearch with ID: {}", productUpdate.getId());
    return BaseResponse.build(productUpdate.getId(), true);
  }

  @Override
  public BaseResponse<Long> updateProductVariant(Long id, UpsertProductVariantRequest request) {
    log.info("Updating product variant with ID {} and {}", id, request.toString());
    ProductVariant productVariant = productVariantService.findById(id);
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopOwner.getId());
    productVariant.updateProductVariant(
        request.getProductSize(),
        request.getQuantity(),
        request.getOriginalPrice(),
        request.getResalePrice(),
        request.getProductCondition());
    productVariant.updateAt();
    productVariant = productVariantService.save(productVariant);
    log.info("Product variant updated with ID: {}", productVariant.getId());

    Product product = productService.findById(productVariant.getProduct().getId());
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info("Product synced to Elasticsearch with ID: {}", product.getId());
    return BaseResponse.build(productVariant.getId(), true);
  }

  @Override
  @Transactional
  public BaseResponse<Void> uploadMultipleProductImages(
      List<MultipartFile> multipartFiles, Long productId) throws IOException {
    Product product = productService.findById(productId);
    log.info("Uploading multiple product images for product ID: {}", productId);
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopOwner.getId());
    List<Map<String, String>> uploadResults = new ArrayList<>();
    for (MultipartFile file : multipartFiles) {
      Map<String, String> uploadResult = cloudinaryService.uploadImage(file.getBytes());
      uploadResults.add(uploadResult);
    }
    if (product.getProductAssets() != null) {
      for (var mediaKey : product.getProductAssets()) {
        cloudinaryService.deleteImage(mediaKey.getPublicId());
      }
      product.getProductAssets().clear();
    }

    List<ProductAsset> newAssets =
        uploadResults.stream()
            .map(
                mediaKey ->
                    ProductAsset.builder()
                        .mediaKey(this.cloudinaryService.getImageUrl(mediaKey.get("asset_id")))
                        .publicId(mediaKey.get("public_id"))
                        .product(product)
                        .build())
            .toList();
    product.getProductAssets().addAll(newAssets);
    product.updateAt();
    productService.save(product);
    log.info("Product images uploaded and saved for product ID: {}", productId);
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info("Product synced to Elasticsearch after image upload for product ID: {}", productId);
    return BaseResponse.ok();
  }

  @Override
  @Transactional
  public BaseResponse<String> uploadProductImageWithImageId(
      MultipartFile multipartFile, Long imageId) throws IOException {
    log.info("Uploading product image with ID: {}", imageId);
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopOwner.getId());
    ProductAsset productAsset = productAssetService.findProductAssetById(imageId);
    if (productAsset.getPublicId() != null) {
      cloudinaryService.deleteImage(productAsset.getPublicId());
    }
    Map<String, String> accessKey = this.cloudinaryService.uploadImage(multipartFile.getBytes());
    productAsset.updateImage(
        this.cloudinaryService.getImageUrl(accessKey.get("asset_id")), accessKey.get("public_id"));
    productAsset.updateAt();
    productAssetService.saveProductAsset(productAsset);
    log.info("Product image uploaded and saved with ID: {}", productAsset.getId());
    Product product = productService.findById(productAsset.getProduct().getId());
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info(
        "Product synced to Elasticsearch after image upload for product ID: {}", product.getId());
    return BaseResponse.build(productAsset.getMediaKey(), true);
  }

  @Override
  public BaseResponse<Void> updateStatusProductVariant(Long id, boolean isActive) {
    log.info("Updating status of product variant with ID: {}, isActive: {}", id, isActive);
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Shop owner ID: {}", shopOwner.getId());
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopOwner.getId());
    ProductVariant productVariant = productVariantService.findById(id);
    productVariant.updateStatus(isActive);
    productVariant.updateAt();
    productVariantService.save(productVariant);
    log.info("Product variant status updated with ID: {}", productVariant.getId());
    Product product = productService.findById(productVariant.getProduct().getId());
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info(
        "Product synced to Elasticsearch after variant status update for product ID: {}",
        product.getId());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> updateStatusProduct(Long id, boolean isActive) {
    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Updating status of product with ID: {}, isActive: {}", id, isActive);
    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopOwner.getId());
    Product product = productService.findById(id);
    product.updateStatus(isActive);
    product.updateAt();
    productService.save(product);
    log.info("Product status updated with ID: {}", product.getId());
    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    log.info(
        "Product synced to Elasticsearch after status update for product ID: {}", product.getId());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<PaginationResponse<List<ProductResponse>>> getProductsWithFilter(
      ProductCriteria criteria, boolean isAdmin) {

    List<ProductElasticsearchDTO> response;
    try {
      log.info("Fetching products with filter: {}", criteria.toString());
      response = productElasticsearch.findByFilter(criteria, isAdmin);

      if (!response.isEmpty()) {
        List<ProductResponse> productResponses =
            response.stream().map(this::convertToProductResponse).collect(Collectors.toList());
        log.info("Found {} products matching the filter", productResponses.size());

        PaginationResponse<List<ProductResponse>> paginationResponse =
            PaginationResponse.buildFromElastic(
                productResponses,
                criteria.getCurrentPage(),
                criteria.getPageSize(),
                productResponses.size(),
                true);

        return BaseResponse.build(paginationResponse, true);
      }

    } catch (Exception e) {
      log.warn(
          "Elasticsearch not available or index not found, falling back to DB: {}", e.getMessage());
    }

    var productResponses = productService.findByFilter(criteria, isAdmin);
    if (productResponses.isEmpty()) throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND);

    List<ProductResponse> result =
        productResponses.getContent().stream().map(this::buildProductResponse).toList();
    log.info("Found {} products matching the filter from database", result.size());

    return BaseResponse.build(
        PaginationResponse.build(result, productResponses, criteria.getPageSize(), true), true);
  }

  @Override
  public BaseResponse<PaginationResponse<List<ProductResponse>>> getProductsByOwner(
      ProductCriteria criteria) {

    log.info("Fetching products by owner with criteria: {}", criteria.toString());

    var shopOwner =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Shop owner ID: {}", shopOwner.getId());

    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + shopOwner.getId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop ID from Cache: {}", shopOwner.getId());
      shopInfo = storeServiceFeign.checkValidShopById(shopOwner.getId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shopID: {}", shopOwner.getId());

    criteria.setShopId(shopInfo.getId());

    List<ProductElasticsearchDTO> response;
    try {
      response = productElasticsearch.findByFilter(criteria, true);
      if (!response.isEmpty()) {
        log.info("Found {} products matching the owner filter", response.size());
        List<ProductResponse> productResponses =
            response.stream().map(this::convertToProductResponse).collect(Collectors.toList());

        PaginationResponse<List<ProductResponse>> paginationResponse =
            PaginationResponse.buildFromElastic(
                productResponses,
                criteria.getCurrentPage(),
                criteria.getPageSize(),
                productResponses.size(),
                true);

        return BaseResponse.build(paginationResponse, true);
      }
    } catch (Exception e) {
      log.warn(
          "Elasticsearch not available or index not found, falling back to DB: {}", e.getMessage());
    }

    var productResponses = productService.findByFilter(criteria, true);
    if (productResponses.isEmpty()) throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND);
    List<ProductResponse> result =
        productResponses.getContent().stream().map(this::buildProductResponse).toList();
    log.info("Found {} products matching the owner filter from database", result.size());

    return BaseResponse.build(
        PaginationResponse.build(result, productResponses, criteria.getPageSize(), true), true);
  }

  @Override
  public BaseResponse<ProductDetailResponse> getProductDetailBy(Long id) {
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    ProductDetailResponse productDetailResponse = null;
    boolean fetchedFromElastic = false;

    boolean isAdmin =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

    try {
      ProductElasticsearchDTO productElasticsearchDTO =
          productElasticsearch.findById(String.valueOf(id));
      log.info("Fetching product detail for ID: {}", id);
      if (productElasticsearchDTO != null) {
        log.info("Product detail found in Elasticsearch for ID: {}", id);
        if (!isAdmin) {
          if (!productElasticsearchDTO.getIsActive()
              || !productElasticsearchDTO.getProductApprovalStatus().isApproved()) {
            log.warn("Product with ID {} is not active or approved", id);
            throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND);
          }
        }
        List<String> productImageUrls =
            Optional.ofNullable(productElasticsearchDTO.getProductAssets())
                .filter(assets -> !assets.isEmpty())
                .map(
                    assets ->
                        assets.stream().map(ProductAssetElasticsearchDTO::getMediaKey).toList())
                .orElse(Collections.emptyList());

        productDetailResponse =
            ProductDetailResponse.builder()
                .id(Long.valueOf(productElasticsearchDTO.getId()))
                .productCode(productElasticsearchDTO.getProductSku())
                .productName(productElasticsearchDTO.getProductName())
                .productDescription(productElasticsearchDTO.getDescription())
                .productStory(productElasticsearchDTO.getStory())
                .viewCount(productElasticsearchDTO.getViewCount())
                .productDetails(
                    (HashMap<String, String>) productElasticsearchDTO.getProductDetails())
                .productOrigin(productElasticsearchDTO.getOrigin())
                .productBrand(productElasticsearchDTO.getBrandName())
                .brandId(productElasticsearchDTO.getBrandId())
                .subCategoryId(productElasticsearchDTO.getSubCategoryId())
                .productSubCategory(productElasticsearchDTO.getSubCategoryName())
                .productImageUrl(productImageUrls)
                .shopId(productElasticsearchDTO.getShopId())
                .productVariants(
                    productElasticsearchDTO.getVariants().stream()
                        .map(this::convertToProductVariantDetailResponseFromElastic)
                        .toList())
                .productApprovalStatus(productElasticsearchDTO.getProductApprovalStatus())
                .productApprovalBy(productElasticsearchDTO.getProductApprovalBy())
                .reasonReject(productElasticsearchDTO.getReasonReject())
                .saleStatus(productElasticsearchDTO.getSaleStatus())
                .createdAt(productElasticsearchDTO.getCreatedAt())
                .updatedAt(productElasticsearchDTO.getUpdatedAt())
                .version(productElasticsearchDTO.getVersion())
                .build();

        fetchedFromElastic = true;
      }

    } catch (Exception e) {
      log.warn(
          "Elasticsearch not available or error fetching product detail for ID {}: {}",
          id,
          e.getMessage());
    }

    if (!fetchedFromElastic) {
      log.info("Fetching product detail from database for ID: {}", id);
      Product product = productService.findById(id);
      if (!isAdmin) {
        if (!product.isActive() || !product.getProductApprovalStatus().isApproved()) {
          log.warn("Product with ID {} is not active or approved", id);
          throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND);
        }
      }

      List<String> productImageUrls =
          Optional.ofNullable(product.getProductAssets())
              .filter(assets -> !assets.isEmpty())
              .map(assets -> assets.stream().map(ProductAsset::getMediaKey).toList())
              .orElse(Collections.emptyList());

      productDetailResponse =
          ProductDetailResponse.builder()
              .id(product.getId())
              .productCode(product.getProductSku())
              .productName(product.getProductName())
              .productDescription(product.getDescription())
              .productStory(product.getStory())
              .viewCount(product.getViewCount())
              .productDetails(product.getProductDetails())
              .productOrigin(product.getOrigin())
              .productBrand(product.getBrand().getBrandName())
              .brandId(product.getBrand().getId())
              .subCategoryId(product.getSubCategory().getId())
              .productSubCategory(product.getSubCategory().getSubCategoryName())
              .productImageUrl(productImageUrls)
              .shopId(product.getShopId())
              .productVariants(
                  product.getProductVariants().stream()
                      .map(this::convertToProductVariantDetailResponseFromDB)
                      .toList())
              .productApprovalStatus(product.getProductApprovalStatus())
              .productApprovalBy(product.getProductApprovalBy())
              .reasonReject(product.getReasonReject())
              .saleStatus(product.getSaleStatus())
              .createdAt(product.getCreatedAt())
              .updatedAt(product.getUpdatedAt())
              .version(product.getVersion())
              .build();
    }

    writeProductViewLog(productDetailResponse, authentication.getId());

    return BaseResponse.build(productDetailResponse, true);
  }

  private void writeProductViewLog(ProductDetailResponse productDetail, Long userId) {
    productQueueProducer.writeProductLogToMongoAndIncrementViewCount(
        ProductLogDetailDTO.builder()
            .productId(productDetail.getId())
            .userId(userId)
            .productName(productDetail.getProductName())
            .description(productDetail.getProductDescription())
            .price(productDetail.getProductVariants().getFirst().getResalePrice())
            .productImageUrl(
                productDetail.getProductImageUrl().isEmpty()
                    ? null
                    : productDetail.getProductImageUrl().getFirst())
            .subCategoryId(productDetail.getSubCategoryId())
            .productSubCategory(productDetail.getProductSubCategory())
            .viewCount(productDetail.getViewCount())
            .productBrand(productDetail.getProductBrand())
            .build());
  }

  @Override
  public BaseResponse<Void> approveProduct(Long id, ProductApprovalRequest request) {
    log.info("Approving product with ID: {}, request: {}", id, request.toString());
    var authentication =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Admin ID: {}, Admin Name: {}", authentication.getId(), authentication.getEmail());

    Product product = productService.findById(id);
    product.updateProductApprovalStatus(
        request.getProductApprovalStatus(), request.getReasonReject(), authentication.getId());
    if (request.getProductApprovalStatus().isApproved()) {
      product.updateStatus(true);
    }
    product.updateAt();
    productService.save(product);

    var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + product.getShopId());
    if (shopInfo == null) {
      log.warn("Shop info not found for shop owner ID from Cache: {}", shopInfo.getId());
      shopInfo = storeServiceFeign.checkValidShopById(product.getShopId());
      if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
    }
    log.info("Shop info retrieved for shop owner ID: {}", shopInfo.getId());
    log.info("Product {} with ID: {}", product.getProductApprovalStatus(), product.getId());

    ProductElasticsearchDTO productElasticsearchDTO =
        convertToElasticsearchDTO(product, shopInfo.getShopName());
    productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);

    log.info("Product synced to Elasticsearch after approval for product ID: {}", product.getId());

    String content =
        String.format(
            "Product %s has been %s by %s. Please check it out.",
            product.getProductName(), request.getProductApprovalStatus(), adminName);
    notificationProducer.sendNotification(
        NotificationDTO.builder()
            .content(content)
            .createdAt(product.getCreatedAt())
            .type(NotificationType.PRODUCT)
            .senderId(adminId)
            .senderName(adminName)
            .receiverId(product.getShopId())
            .receiverName(shopInfo.getShopName())
            .build());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> syncManualProduct(SyncProductRequest request) {
    if (request.getProductIds().isEmpty()) {
      log.error("Product ID is required for manual sync");
      throw new ProductException(ErrorCode.PRODUCT_ID_NOT_FOUND);
    }
    for (var productId : request.getProductIds()) {
      Product product = productService.findById(productId);
      ProductElasticsearchDTO productElasticsearchDTO = convertToElasticsearchDTO(product, null);
      productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
      log.info("Product synced to Elasticsearch with ID: {}", product.getId());
    }

    return BaseResponse.ok();
  }

  @Override
  public ResponseEntity<List<ProductValidDTO>> validateProducts(
      List<ValidateOrderItemRequest> request) {
    log.info("Validating products with request: {}", request.toString());
    List<ProductValidDTO> validDTOList = new ArrayList<>();
    for (ValidateOrderItemRequest item : request) {
      ProductVariant product =
          productVariantService.validateProductVariant(
              item.getProductVariantId(), item.getQuantity(), item.getVersion());
      if (product == null) {
        log.warn("Product not valid: {}", item.getProductVariantId());
        return ResponseEntity.ok(Collections.emptyList());
      }
      log.info("Product valid: {}", item.getProductVariantId());
      validDTOList.add(
          ProductValidDTO.builder()
              .productValidId(item.getProductVariantId())
              .isValid(true)
              .productName(product.getProduct().getProductName())
              .imageUrl(product.getProduct().getProductAssets().getFirst().getMediaKey())
              .productCode(product.getProduct().getProductSku())
              .quantity(item.getQuantity())
              .price(product.getResalePrice())
              .build());
    }
    return ResponseEntity.ok(validDTOList);
  }

  @Override
  public ResponseEntity<Boolean> updateProductQuantity(List<ValidateOrderItemRequest> request) {
    log.info("Updating product quantity with request: {}", request.toString());
    for (ValidateOrderItemRequest item : request) {
      ProductVariant productVariant = productVariantService.findById(item.getProductVariantId());
      if (productVariant == null) {
        log.warn("Product variant not found: {}", item.getProductVariantId());
        return ResponseEntity.ok(false);
      }
      if (productVariant.getQuantity() < item.getQuantity()) {
        log.warn(
            "Not enough quantity for product variant ID: {}, available: {}, requested: {}",
            item.getProductVariantId(),
            productVariant.getQuantity(),
            item.getQuantity());
        return ResponseEntity.ok(false);
      }
      productVariant.updateQuantity(productVariant.getQuantity() - item.getQuantity());
      if (productVariant.getQuantity() <= 0) {
        productVariant.updateStatus(false);
      }
      productVariantService.save(productVariant);
      log.info(
          "Updated quantity for product variant ID: {}, new quantity: {}",
          item.getProductVariantId(),
          productVariant.getQuantity());

      Long idProduct = productVariant.getProduct().getId();

      Product product = productService.findById(idProduct);
      var shopInfo = cacheService.retrieveShopSnapshot(shopKeyCachePrefix + product.getShopId());
      if (shopInfo == null) {
        log.warn("Shop info not found for shop owner ID from Cache: {}", product.getShopId());
        shopInfo = storeServiceFeign.getShopInfoById(product.getShopId());
        if (shopInfo == null) throw new StoreException(ErrorCode.STORE_NOT_FOUND);
      }
      ProductElasticsearchDTO productElasticsearchDTO =
          convertToElasticsearchDTO(product, shopInfo.getShopName());
      productQueueProducer.syncProductToElasticsearch(productElasticsearchDTO);
    }
    return ResponseEntity.ok(true);
  }

  private ProductVariantDetailResponse convertToProductVariantDetailResponseFromDB(
      ProductVariant productVariant) {
    return ProductVariantDetailResponse.builder()
        .id(productVariant.getId())
        .size(productVariant.getProductSize())
        .quantity(productVariant.getQuantity())
        .originalPrice(productVariant.getOriginalPrice())
        .resalePrice(productVariant.getResalePrice())
        .condition(productVariant.getCondition())
        .isActive(productVariant.isActive())
        .version(productVariant.getVersion())
        .build();
  }

  private ProductVariantDetailResponse convertToProductVariantDetailResponseFromElastic(
      ProductVariantElasticsearchDTO productVariant) {
    return ProductVariantDetailResponse.builder()
        .id(Long.valueOf(productVariant.getId()))
        .size(productVariant.getProductSize())
        .quantity(productVariant.getQuantity())
        .originalPrice(productVariant.getOriginalPrice())
        .resalePrice(productVariant.getResalePrice())
        .condition(productVariant.getCondition())
        .isActive(productVariant.isActive())
        .version(productVariant.getVersion())
        .build();
  }

  private ProductResponse buildProductResponse(Product product) {
    String imageUrl =
        !product.getProductAssets().isEmpty()
            ? product.getProductAssets().getFirst().getMediaKey()
            : imageDefaultProduct;
    return ProductResponse.builder()
        .id(product.getId())
        .shopId(product.getShopId())
        .shopName(product.getProductName())
        .productCode(product.getProductSku())
        .name(product.getProductName())
        .productCondition(product.getProductVariants().getFirst().getCondition())
        .imageUrl(imageUrl)
        .resalePrice(product.getProductVariants().getFirst().getResalePrice())
        .isActive(product.isActive())
        .build();
  }

  private ProductResponse convertToProductResponse(ProductElasticsearchDTO dto) {
    String imageUrl =
        !dto.getProductAssets().isEmpty()
            ? dto.getProductAssets().getFirst().getMediaKey()
            : imageDefaultProduct;
    return ProductResponse.builder()
        .id(Long.valueOf(dto.getId()))
        .productCode(dto.getProductSku())
        .shopId(dto.getShopId())
        .shopName(dto.getShopName())
        .name(dto.getProductName())
        .productCondition(dto.getVariants().getFirst().getCondition())
        .imageUrl(imageUrl)
        .resalePrice(
            dto.getVariants() != null && !dto.getVariants().isEmpty()
                ? dto.getVariants().getFirst().getResalePrice()
                : null)
        .isActive(dto.getIsActive())
        .version(dto.getVersion())
        .build();
  }

  public String generateProductCode() {
    LocalDate today = LocalDate.now();
    String datePart = today.format(DateTimeFormatter.ofPattern("yyMMdd"));

    Random random = new Random();
    int randomNumber = 1000 + random.nextInt(9000);

    return "GR" + datePart + randomNumber;
  }

  public ProductElasticsearchDTO convertToElasticsearchDTO(Product product, String shopName) {
    ProductElasticsearchDTO.ProductElasticsearchDTOBuilder builder =
        ProductElasticsearchDTO.builder()
            .id(product.getId().toString())
            .productSku(product.getProductSku())
            .productName(product.getProductName())
            .description(product.getDescription())
            .story(product.getStory())
            .viewCount(product.getViewCount())
            .productDetails(
                product.getProductDetails() != null
                    ? new HashMap<>(product.getProductDetails())
                    : new HashMap<>())
            .origin(product.getOrigin())
            .saleStatus(product.getSaleStatus())
            .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
            .brandId(product.getBrand().getId())
            .shopId(product.getShopId())
            .subCategoryId(product.getSubCategory().getId())
            .subCategoryName(product.getSubCategory().getSubCategoryName())
            .productAssets(mapAssets(product.getProductAssets()))
            .variants(mapVariants(product.getProductVariants()))
            .productApprovalStatus(product.getProductApprovalStatus())
            .productApprovalBy(product.getProductApprovalBy())
            .reasonReject(product.getReasonReject())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .version(product.getVersion())
            .isActive(product.isActive());

    if (shopName != null) {
      builder.shopName(shopName);
    }

    return builder.build();
  }

  private List<ProductAssetElasticsearchDTO> mapAssets(List<ProductAsset> assets) {
    if (assets == null) return Collections.emptyList();

    return assets.stream()
        .map(
            asset ->
                ProductAssetElasticsearchDTO.builder()
                    .id(asset.getId().toString())
                    .publicId(asset.getPublicId())
                    .mediaKey(asset.getMediaKey())
                    .build())
        .collect(Collectors.toList());
  }

  private List<ProductVariantElasticsearchDTO> mapVariants(List<ProductVariant> variants) {
    if (variants == null) return Collections.emptyList();

    return variants.stream()
        .map(
            variant ->
                ProductVariantElasticsearchDTO.builder()
                    .id(variant.getId().toString())
                    .productSize(variant.getProductSize())
                    .quantity(variant.getQuantity())
                    .originalPrice(variant.getOriginalPrice())
                    .resalePrice(variant.getResalePrice())
                    .condition(variant.getCondition())
                    .isActive(variant.isActive())
                    .version(variant.getVersion())
                    .build())
        .collect(Collectors.toList());
  }
}

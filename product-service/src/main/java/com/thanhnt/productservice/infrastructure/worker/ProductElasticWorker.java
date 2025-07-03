package com.thanhnt.productservice.infrastructure.worker;

import com.thanhnt.productservice.application.dto.ProductAssetElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductVariantElasticsearchDTO;
import com.thanhnt.productservice.application.service.ProductQueueProducer;
import com.thanhnt.productservice.application.service.ProductService;
import com.thanhnt.productservice.domain.entity.product.Product;
import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;
import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductElasticWorker {
  private ProductService productService;
  private ProductQueueProducer productQueueProducer;

  private static final Logger log = LoggerFactory.getLogger(ProductElasticWorker.class);

  public ProductElasticWorker(
      ProductService productService, ProductQueueProducer productQueueProducer) {
    this.productService = productService;
    this.productQueueProducer = productQueueProducer;
  }

  @Scheduled(cron = "0 0/10 * * * ?")
  public void syncProductsToElastic() {
    log.info("Bắt đầu đồng bộ sản phẩm cập nhật trong 10 phút gần nhất...");

    long tenMinutesAgo = System.currentTimeMillis() - (10 * 60 * 1000);

    List<Product> result = productService.findByUpdatedAtGreaterThan(tenMinutesAgo);

    List<Product> updatedProducts =
        result.stream()
            .filter(product -> !product.getCreatedAt().equals(product.getUpdatedAt()))
            .toList();

    if (updatedProducts.isEmpty()) {
      log.info("Không có sản phẩm nào cần đồng bộ.");
    } else {
      for (Product product : updatedProducts) {

        try {
          productQueueProducer.syncProductToElasticsearch(convertToElasticsearchDTO(product, null));
        } catch (Exception e) {
          log.error(
              "Lỗi khi gửi sản phẩm {} lên ElasticSearch: {}", product.getId(), e.getMessage());
        }
      }
      log.info("Đồng bộ {} sản phẩm lên Elasticsearch.", updatedProducts.size());
    }
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

package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductLogDetailDTO;
import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.application.service.CacheService;
import com.thanhnt.productservice.application.service.ProductElasticsearch;
import com.thanhnt.productservice.application.service.ProductLogService;
import com.thanhnt.productservice.application.service.ProductQueueConsumer;
import com.thanhnt.productservice.domain.logs.LogDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueueConsumerImpl implements ProductQueueConsumer {
  private final ProductElasticsearch productElasticsearch;
  private final ProductLogService productLogService;
  private final CacheService cacheService;
  private final String KEY_PRODUCT_VIEW = "product:view:";

  @Override
  @RabbitListener(queues = {"${rabbitmq.product-elastic-queue}"})
  public void consumeProductToElasticsearch(ProductElasticsearchDTO product) {
    log.info("Consuming product to Elasticsearch: " + product.getId());
    productElasticsearch.createOrUpdateProduct(product);
  }

  @Override
  @RabbitListener(queues = {"${rabbitmq.product-log-queue}"})
  @Retryable(
      value = {ProductException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 8000))
  public void consumeProductLogToMongoAndIncrementViewCount(
      ProductLogDetailDTO productLogDetailDTO) {
    log.info("Consuming product log to MongoDB: " + productLogDetailDTO.getProductId());
    productLogService.saveProductLog(
        LogDetail.builder()
            .productId(productLogDetailDTO.getProductId())
            .productBrand(productLogDetailDTO.getProductBrand())
            .productName(productLogDetailDTO.getProductName())
            .userId(productLogDetailDTO.getUserId())
            .productImageUrl(productLogDetailDTO.getProductImageUrl())
            .price(productLogDetailDTO.getPrice())
            .description(productLogDetailDTO.getDescription())
            .subCategoryId(productLogDetailDTO.getSubCategoryId())
            .productSubCategory(productLogDetailDTO.getProductSubCategory())
            .build());
    cacheService.storeProductAndIncreaseView(KEY_PRODUCT_VIEW + productLogDetailDTO.getProductId());
  }
}

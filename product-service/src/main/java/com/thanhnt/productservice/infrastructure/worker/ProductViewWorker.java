package com.thanhnt.productservice.infrastructure.worker;

import com.thanhnt.productservice.application.service.CacheService;
import com.thanhnt.productservice.application.service.ProductService;
import java.util.Set;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductViewWorker {

  private final CacheService cacheService;
  private final ProductService productService;

  private static final String VIEW_PREFIX = "product:view:";

  public ProductViewWorker(CacheService cacheService, ProductService productService) {
    this.cacheService = cacheService;
    this.productService = productService;
  }

  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void syncProductViewsToDatabase() {
    Set<String> keys = cacheService.getAllProductViewKeys(VIEW_PREFIX + "*");

    if (keys == null || keys.isEmpty()) return;

    for (String key : keys) {
      try {
        String productIdStr = key.replace(VIEW_PREFIX, "");
        Long productId = Long.parseLong(productIdStr);

        Object viewCountObj = cacheService.retrieveProduct(key);
        long viewCount = viewCountObj == null ? 0 : Long.parseLong(viewCountObj.toString());

        productService.updateViewProduct(productId, viewCount);

      } catch (Exception e) {
        System.err.println("Lỗi khi sync view cho key " + key + ": " + e.getMessage());
      }
    }
  }
}

package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductLogDetailDTO;

public interface ProductQueueConsumer {
  void consumeProductToElasticsearch(ProductElasticsearchDTO product);

  void consumeProductLogToMongoAndIncrementViewCount(ProductLogDetailDTO productLogDetailDTO);
}

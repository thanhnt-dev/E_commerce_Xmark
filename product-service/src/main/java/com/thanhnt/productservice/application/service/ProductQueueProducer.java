package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductLogDetailDTO;

public interface ProductQueueProducer {
  void syncProductToElasticsearch(ProductElasticsearchDTO product);

  void writeProductLogToMongoAndIncrementViewCount(ProductLogDetailDTO productLogDetailDTO);
}

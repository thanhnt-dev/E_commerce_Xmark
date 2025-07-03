package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.api.request.ProductCriteria;
import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import java.io.IOException;
import java.util.List;

public interface ProductElasticsearch {
  void createOrUpdateProduct(ProductElasticsearchDTO product);

  ProductElasticsearchDTO findById(String productId);

  String deleteById(String productId);

  List<ProductElasticsearchDTO> findByFilter(ProductCriteria criteria, boolean isAdmin)
      throws IOException;
}

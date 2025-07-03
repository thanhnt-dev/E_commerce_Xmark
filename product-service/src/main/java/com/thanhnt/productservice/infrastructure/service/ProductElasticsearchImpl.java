package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.api.request.ProductCriteria;
import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.service.ProductElasticsearch;
import com.thanhnt.productservice.domain.repository.elasticsearch.ProductElasticsearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductElasticsearchImpl implements ProductElasticsearch {
  private final ProductElasticsearchRepository productElasticsearchRepository;

  @Override
  public void createOrUpdateProduct(ProductElasticsearchDTO product) {
    productElasticsearchRepository.createOrUpdateProduct(product);
  }

  @Override
  public ProductElasticsearchDTO findById(String productId) {
    return productElasticsearchRepository.findById(productId);
  }

  @Override
  public String deleteById(String productId) {
    return productElasticsearchRepository.deleteById(productId);
  }

  @Override
  public List<ProductElasticsearchDTO> findByFilter(ProductCriteria criteria, boolean isAdmin) {
    return productElasticsearchRepository.searchProducts(criteria, isAdmin);
  }
}

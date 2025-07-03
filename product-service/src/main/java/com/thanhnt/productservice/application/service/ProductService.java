package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.api.request.ProductCriteria;
import com.thanhnt.productservice.application.dto.AdminProductStatsDTO;
import com.thanhnt.productservice.domain.entity.product.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

  Product save(Product product);

  Product findById(Long id);

  Page<Product> findByFilter(ProductCriteria criteria, boolean isAdmin);

  void updateViewProduct(Long productId, Long viewCount);

  List<Product> findByUpdatedAtGreaterThan(long updatedAt);

  AdminProductStatsDTO getAdminProductStats();
}

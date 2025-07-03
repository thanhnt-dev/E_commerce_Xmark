package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.api.request.ProductCriteria;
import com.thanhnt.productservice.application.dto.AdminProductStatsDTO;
import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.application.service.ProductService;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.product.Product;
import com.thanhnt.productservice.domain.repository.ProductRepository;
import com.thanhnt.productservice.domain.specifications.ProductSpecifications;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public Product save(Product product) {
    return productRepository.save(product);
  }

  @Override
  @Transactional
  public Product findById(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
  }

  @Override
  public Page<Product> findByFilter(ProductCriteria criteria, boolean isAdmin) {
    Pageable pageable =
        PageRequest.of(Math.max(criteria.getCurrentPage() - 1, 0), criteria.getPageSize());
    Specification<Product> specification = ProductSpecifications.baseSpecification(isAdmin);
    if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
      specification =
          specification.and(
              ProductSpecifications.filterByPrice(criteria.getMinPrice(), criteria.getMaxPrice()));
    }
    if (criteria.getBrandId() != null) {
      specification =
          specification.and(ProductSpecifications.filterByBrandId(criteria.getBrandId()));
    }
    if (criteria.getSearch() != null) {
      specification = specification.and(ProductSpecifications.filterByName(criteria.getSearch()));
    }
    if (criteria.getSubCategory() != null) {
      specification =
          specification.and(ProductSpecifications.filterByCategory(criteria.getSubCategory()));
    }
    if (criteria.getSort() != null) {
      specification =
          specification.and(ProductSpecifications.sort(criteria.getSort().split("\\.")));
    }
    if (criteria.getShopId() != null) {
      specification =
          specification.and(ProductSpecifications.filterByStoreId(criteria.getShopId()));
    }

    if (criteria.getApprovalStatus() != null) {
      specification =
          specification.and(
              ProductSpecifications.filterByProductApprovalStatus(criteria.getApprovalStatus()));
    }

    return productRepository.findAll(specification, pageable);
  }

  @Override
  public void updateViewProduct(Long productId, Long viewCount) {
    productRepository.updateViewCount(productId, viewCount);
  }

  @Override
  public List<Product> findByUpdatedAtGreaterThan(long updatedAt) {
    return productRepository.findByUpdatedAtGreaterThan(updatedAt);
  }

  @Override
  public AdminProductStatsDTO getAdminProductStats() {
    Long totalProducts = productRepository.count();

    Long totalApproved =
        productRepository.countByProductApprovalStatus(ProductApprovalStatus.APPROVED);
    Long totalPending =
        productRepository.countByProductApprovalStatus(ProductApprovalStatus.PENDING);
    Long totalRejected =
        productRepository.countByProductApprovalStatus(ProductApprovalStatus.REJECTED);

    LocalDateTime startOfLastMonth =
        LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
    LocalDateTime endOfLastMonth =
        startOfLastMonth.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    ZoneId zone = ZoneId.systemDefault();
    Long startMillis = startOfLastMonth.atZone(zone).toInstant().toEpochMilli();
    Long endMillis = endOfLastMonth.atZone(zone).toInstant().toEpochMilli();

    Long lastMonthProducts = productRepository.countByCreatedAtBetween(startMillis, endMillis);
    Long productsBeforeLastMonth = productRepository.countByCreatedAtBefore(startMillis);

    double growthPercentage = 0.0;
    if (productsBeforeLastMonth != 0) {
      growthPercentage = ((double) lastMonthProducts / productsBeforeLastMonth) * 100;
    }

    return AdminProductStatsDTO.builder()
        .totalProducts(totalProducts)
        .growthPercentage(growthPercentage)
        .lastMonthProducts(lastMonthProducts)
        .totalProductsApproved(totalApproved)
        .totalProductsPendingApproval(totalPending)
        .totalProductsRejected(totalRejected)
        .build();
  }
}

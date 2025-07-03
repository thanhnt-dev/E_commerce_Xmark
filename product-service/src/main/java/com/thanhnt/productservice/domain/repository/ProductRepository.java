package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.product.Product;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
    extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  @Modifying
  @Transactional
  @Query("UPDATE Product p SET p.viewCount = :viewCount WHERE p.id = :productId")
  void updateViewCount(@Param("productId") Long productId, @Param("viewCount") Long viewCount);

  List<Product> findByUpdatedAtGreaterThan(long updatedAt);

  Long countByProductApprovalStatus(ProductApprovalStatus status);

  Long countByCreatedAtBetween(Long startMillis, Long endMillis);

  Long countByCreatedAtBefore(Long millis);

  @Query(
      """
  SELECT p FROM Product p
  LEFT JOIN FETCH p.brand
  LEFT JOIN FETCH p.subCategory
  LEFT JOIN FETCH p.productAssets
  LEFT JOIN FETCH p.productVariants
  WHERE p.id = :id
""")
  Optional<Product> findWithRelationsById(@Param("id") Long id);
}

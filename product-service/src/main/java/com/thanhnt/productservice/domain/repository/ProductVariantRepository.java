package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
  @Query(
      """
    SELECT pv FROM ProductVariant pv
    JOIN FETCH pv.product
    WHERE pv.id = :id AND pv.version = :version AND pv.quantity >= :quantity
""")
  ProductVariant findWithProduct(
      @Param("id") Long id, @Param("version") Integer version, @Param("quantity") Integer quantity);
}

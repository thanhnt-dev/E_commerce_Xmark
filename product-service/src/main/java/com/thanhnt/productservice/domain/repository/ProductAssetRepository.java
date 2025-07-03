package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, Long> {}

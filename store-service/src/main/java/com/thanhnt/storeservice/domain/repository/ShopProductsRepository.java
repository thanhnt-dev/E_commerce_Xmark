package com.thanhnt.storeservice.domain.repository;

import com.thanhnt.storeservice.domain.entity.shopproduct.ShopProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopProductsRepository extends JpaRepository<ShopProducts, Long> {}

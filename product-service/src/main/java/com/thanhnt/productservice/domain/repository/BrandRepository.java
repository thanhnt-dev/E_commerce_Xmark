package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {}

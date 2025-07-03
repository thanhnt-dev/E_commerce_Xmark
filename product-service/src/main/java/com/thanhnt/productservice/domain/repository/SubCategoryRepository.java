package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
  List<SubCategory> findSubCategoryByCategoryId(Long categoryId);
}

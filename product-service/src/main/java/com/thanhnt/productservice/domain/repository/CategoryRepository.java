package com.thanhnt.productservice.domain.repository;

import com.thanhnt.productservice.domain.entity.category.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByParentId(Long parentId);
}

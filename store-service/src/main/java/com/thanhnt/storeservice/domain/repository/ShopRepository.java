package com.thanhnt.storeservice.domain.repository;

import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {
  boolean existsByOwner(Long ownerId);

  Optional<Shop> findByOwner(Long ownerId);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  Long countByVerificationStatus(VerificationStatus status);

  Long countByCreatedAtBetween(Long startMillis, Long endMillis);

  Long countByCreatedAtBefore(Long millis);

  Long countByIsActive(boolean isActive);
}

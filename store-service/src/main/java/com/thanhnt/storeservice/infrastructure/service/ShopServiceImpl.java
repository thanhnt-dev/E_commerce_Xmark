package com.thanhnt.storeservice.infrastructure.service;

import com.thanhnt.storeservice.api.request.StoreCriteria;
import com.thanhnt.storeservice.application.dto.AdminSellerStatsDTO;
import com.thanhnt.storeservice.application.exception.ShopException;
import com.thanhnt.storeservice.application.service.ShopService;
import com.thanhnt.storeservice.domain.entity.common.ErrorCode;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import com.thanhnt.storeservice.domain.repository.ShopRepository;
import com.thanhnt.storeservice.domain.specifications.StoreSpecifications;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {
  private final ShopRepository shopRepository;

  @Override
  public boolean existsByOwnerId(Long ownerId) {
    return shopRepository.existsByOwner(ownerId);
  }

  @Override
  public boolean existsByEmail(String email) {
    return shopRepository.existsByEmail(email);
  }

  @Override
  public boolean existByPhone(String phone) {
    return shopRepository.existsByPhone(phone);
  }

  @Override
  public Shop findByOwnerId(Long ownerId) {
    return shopRepository.findByOwner(ownerId).orElse(null);
  }

  @Override
  public Shop findById(Long id) {
    return shopRepository
        .findById(id)
        .orElseThrow(() -> new ShopException(ErrorCode.SHOP_NOT_FOUND));
  }

  @Override
  @Transactional
  public Shop saveShop(Shop shop) {
    return shopRepository.save(shop);
  }

  @Override
  public Page<Shop> findByFilter(StoreCriteria criteria) {
    Pageable pageable =
        PageRequest.of(Math.max(criteria.getCurrentPage() - 1, 0), criteria.getPageSize());

    Specification<Shop> specification = StoreSpecifications.baseSpecification();
    if (criteria.getSearch() != null) {
      specification = specification.and(StoreSpecifications.search(criteria.getSearch()));
    }
    if (criteria.getBusinessType() != null) {
      specification =
          specification.and(StoreSpecifications.businessType(criteria.getBusinessType()));
    }

    if (criteria.getVerificationStatus() != null) {
      specification =
          specification.and(
              StoreSpecifications.verificationStatus(criteria.getVerificationStatus()));
    }
    if (criteria.getSort() != null) {
      specification = specification.and(StoreSpecifications.sort(criteria.getSort()));
    }

    specification =
        specification.and(
            StoreSpecifications.filterByCreatedAt(
                criteria.getStartCreatedDate(), criteria.getEndCreatedDate()));

    return shopRepository.findAll(specification, pageable);
  }

  @Override
  public AdminSellerStatsDTO getAdminSellerStats() {
    Long totalSellers = shopRepository.count();

    Long totalSellersApproved =
        shopRepository.countByVerificationStatus(VerificationStatus.REJECTED);
    Long totalSellersPendingApproval =
        shopRepository.countByVerificationStatus(VerificationStatus.PENDING);
    Long totalSellersRejected =
        shopRepository.countByVerificationStatus(VerificationStatus.REJECTED);

    LocalDateTime startOfLastMonth =
        LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
    LocalDateTime endOfLastMonth =
        startOfLastMonth.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

    ZoneId zone = ZoneId.systemDefault();
    Long startMillis = startOfLastMonth.atZone(zone).toInstant().toEpochMilli();
    Long endMillis = endOfLastMonth.atZone(zone).toInstant().toEpochMilli();

    Long lastMonthSellers = shopRepository.countByCreatedAtBetween(startMillis, endMillis);
    Long sellersBeforeLastMonth = shopRepository.countByCreatedAtBefore(startMillis);

    Long totalSellersActive = shopRepository.countByIsActive(true);
    Long totalSellersInactive = shopRepository.countByIsActive(false);

    double growthPercentage = 0.0;
    if (sellersBeforeLastMonth != 0) {
      growthPercentage = ((double) (lastMonthSellers) / sellersBeforeLastMonth) * 100;
    }

    return AdminSellerStatsDTO.builder()
        .totalSellers(totalSellers)
        .totalSellersApproved(totalSellersApproved)
        .totalSellersPendingApproval(totalSellersPendingApproval)
        .totalInactiveSellers(totalSellersInactive)
        .totalActiveSellers(totalSellersActive)
        .totalSellersRejected(totalSellersRejected)
        .lastMonthSellers(lastMonthSellers)
        .growthPercentage(growthPercentage)
        .build();
  }
}

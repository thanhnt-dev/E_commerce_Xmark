package com.thanhnt.storeservice.api.request;

import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCriteria extends BaseCriteria {
  private Integer currentPage;
  private Integer pageSize;
  private String sort;
  private BusinessType businessType;
  private VerificationStatus verificationStatus;
  private String search;

  private Long startCreatedDate;
  private Long endCreatedDate;
}

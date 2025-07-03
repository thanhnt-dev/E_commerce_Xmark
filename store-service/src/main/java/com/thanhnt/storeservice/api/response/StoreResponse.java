package com.thanhnt.storeservice.api.response;

import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
  private Long id;
  private String name;
  private VerificationStatus verificationStatus;
  private BusinessType businessType;
  private Long createdDate;
}

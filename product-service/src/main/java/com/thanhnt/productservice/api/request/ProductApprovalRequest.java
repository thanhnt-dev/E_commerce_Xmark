package com.thanhnt.productservice.api.request;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ProductApprovalRequest {
  private ProductApprovalStatus productApprovalStatus;
  private String reasonReject;
}

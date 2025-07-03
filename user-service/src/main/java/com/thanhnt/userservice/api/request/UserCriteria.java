package com.thanhnt.userservice.api.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteria extends BaseCriteria {
  private Integer currentPage;
  private Integer pageSize;
  private String phoneNumber;
  private String mail;
}

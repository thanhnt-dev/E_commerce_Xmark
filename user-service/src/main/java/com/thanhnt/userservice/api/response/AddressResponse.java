package com.thanhnt.userservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressResponse {
  private long id;
  private String name;
  private String phone;
  private String type;
  private String detail;
  private String ward;
  private String district;
  private String province;
}

package com.thanhnt.storeservice.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StoreDetailsResponse {
  private Long id;
  private String name;
  private String address;
  private String phone;
  private String email;
  private String description;
  private String businessType;
  private String verificationStatus;
  private String identityNumber;
  private Long category;
  private String frontIdentityNumber;
  private String backIdentityNumber;
  private Long createdDate;
  private String nameOwner;
  private Long dateOfBirthOwner;
  private String phoneOwner;
  private String emailOwner;
  private List<StoreRequestResponse> storeRequests;
}

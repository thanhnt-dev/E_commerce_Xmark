package com.thanhnt.userservice.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserProfileResponse {
  private String email;
  private String firstName;
  private String lastName;
  private String gender;
  private String phone;
  private String avatar;
  private boolean isEnableTwoFactor;
  private List<AddressResponse> addresses;
  private Long dateOfBirth;
}

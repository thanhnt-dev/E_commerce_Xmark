package com.thanhnt.userservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSnapshotDTO {
  private Long id;
  private String fullName;
  private String email;
  private String phone;
  private String avatar;
}

package com.thanhnt.messageservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSnapshotDTO {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("fullName")
  private String fullName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("avatar")
  private String avatar;
}

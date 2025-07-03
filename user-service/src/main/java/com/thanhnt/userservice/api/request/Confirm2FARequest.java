package com.thanhnt.userservice.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Confirm2FARequest {
  @NotNull private boolean twoFA;
}

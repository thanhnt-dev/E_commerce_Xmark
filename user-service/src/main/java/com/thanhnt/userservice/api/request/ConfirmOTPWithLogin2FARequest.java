package com.thanhnt.userservice.api.request;

import com.thanhnt.userservice.domain.entity.commons.OTPType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ConfirmOTPWithLogin2FARequest {

  @NotNull(message = "Token is required")
  private String token;

  @NotNull(message = "OTP code is required")
  @NotBlank(message = "OTP code cannot be blank")
  @Size(min = 6, max = 6, message = "OTP code must be 6 number digits")
  @Schema(example = "000000")
  private String otp;

  @NotNull(message = "OTP type is require")
  private OTPType type;
}

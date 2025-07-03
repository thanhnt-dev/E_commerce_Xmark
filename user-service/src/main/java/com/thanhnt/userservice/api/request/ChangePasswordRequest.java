package com.thanhnt.userservice.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ChangePasswordRequest {
  @NotNull(message = "Old password is required")
  @NotBlank(message = "Old password cannot be blank")
  private String oldPassword;

  @NotNull(message = "New password is required")
  @NotBlank(message = "New password cannot be blank")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message =
          "The new password must be at least 8 characters, including letters, numbers, and special characters.")
  @Schema(description = "password", example = "NguyenThanhSr4@")
  private String newPassword;

  @NotNull(message = "Confirm password is required")
  @NotBlank(message = "Confirm password cannot be blank")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message =
          "The confirm password must be at least 8 characters, including letters, numbers, and special characters.")
  @Schema(description = "password", example = "NguyenThanhSr4@")
  private String confirmPassword;
}

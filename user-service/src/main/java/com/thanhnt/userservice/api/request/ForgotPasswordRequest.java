package com.thanhnt.userservice.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ForgotPasswordRequest {
  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be blank")
  @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "The email format is incorrect.")
  @Schema(description = "email", example = "email@email.com")
  private String email;
}

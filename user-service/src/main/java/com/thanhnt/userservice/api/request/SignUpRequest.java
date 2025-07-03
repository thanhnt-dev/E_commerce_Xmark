package com.thanhnt.userservice.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpRequest {
  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be blank")
  @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "The email format is incorrect.")
  @Schema(description = "email", example = "email@email.com")
  private String email;

  //  @NotNull(message = "First name is required")
  //  @NotBlank(message = "First name cannot be blank")
  //  @Pattern(regexp = "^[A-Za-z ]{2,30}$", message = "The first name format is incorrect.")
  //  @Schema(description = "firstName", example = "Thanh")
  //  private String firstName;
  //
  //  @NotNull(message = "Last name is required")
  //  @NotBlank(message = "Last name cannot be blank")
  //  @Pattern(regexp = "^[A-Za-z ]{2,30}$", message = "The last name format is incorrect.")
  //  @Schema(description = "lastName", example = "Nguyen Tan")
  //  private String lastName;

  @NotNull(message = "Phone is required")
  @NotBlank(message = "Phone cannot be blank")
  @Pattern(regexp = "^\\d{10}$", message = "The phone number must be 10 digits.")
  @Schema(description = "phone", example = "0123456789")
  private String phone;

  //  @NotNull(message = "Date of birth is required")
  //  @Schema(description = "Date of birth", example = "01/01/2003")
  //  private Long dateOfBirth;

  @NotNull(message = "Password is required")
  @NotBlank(message = "Password cannot be blank")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message =
          "The password must be at least 8 characters, including letters, numbers, and special characters.")
  @Schema(description = "password", example = "NguyenThanhSr4@")
  private String password;

  @NotNull(message = "Confirm password is required")
  @NotBlank(message = "Confirm password cannot be blank")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message =
          "The confirm password must match the required format of at least 8 characters, including letters, numbers, and special characters.")
  @Schema(description = "Confirm password", example = "NguyenThanhSr4@")
  private String confirmPassword;
}

package com.thanhnt.userservice.api.request;

import com.thanhnt.userservice.domain.entity.commons.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProfileRequest {
  @NotNull(message = "Phone is require")
  @NotBlank(message = "Phone can not be blank")
  @Pattern(regexp = "^\\d{10}$\n", message = "The phone number must be 10 number.")
  @Schema(description = "phone", example = "0123456789")
  private String phone;

  @Schema(description = "First name of the user", example = "John")
  private String firstName;

  @Schema(description = "Last name of the user", example = "Doe")
  private String lastName;

  @NotNull(message = "Date Of Birth is require")
  @NotBlank(message = "Date Of Birth can not be blank")
  @Pattern(
      regexp = "^(0[1-9]|1[0-2])/(0[1-9]|1\\d|2\\d|3[01])/2006$\n",
      message = "The date of birth is incorrect.")
  @Schema(description = "Date of birth", example = "01/01/2003")
  private Long dateOfBirth;

  private Gender gender;
  private boolean enableTwoFactor;
}

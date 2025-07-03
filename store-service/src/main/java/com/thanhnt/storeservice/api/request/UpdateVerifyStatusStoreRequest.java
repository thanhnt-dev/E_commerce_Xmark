package com.thanhnt.storeservice.api.request;

import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateVerifyStatusStoreRequest {
  @NotNull(message = "Reason is required")
  @NotBlank(message = "Reason cannot be blank")
  @Schema(description = "Reason", example = "Oke!")
  private String reason;

  @NotNull(message = "VerificationStatus is required")
  @NotBlank(message = "VerificationStatus cannot be blank")
  private VerificationStatus verificationStatus;
}

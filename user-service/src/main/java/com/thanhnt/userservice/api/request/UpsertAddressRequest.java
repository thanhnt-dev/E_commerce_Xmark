package com.thanhnt.userservice.api.request;

import com.thanhnt.userservice.domain.entity.address.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class UpsertAddressRequest {

  private long id;

  @NotNull(message = "Name is required")
  @NotBlank(message = "Name cannot be blank")
  @Schema(example = "Nguyễn Văn A")
  private String name;

  @NotNull(message = "Phone is required")
  @NotBlank(message = "Phone cannot be blank")
  @Schema(example = "0123456789")
  private String phone;

  @NotNull(message = "Type is required")
  private AddressType addressType;

  @NotNull(message = "Detail is required")
  @NotBlank(message = "Detail cannot be blank")
  @Schema(example = "123/4/5")
  private String detail;

  @NotNull(message = "Ward is required")
  @NotBlank(message = "Ward cannot be blank")
  @Schema(example = "Phuoc Long A")
  private String ward;

  @NotNull(message = "District is required")
  @NotBlank(message = "District cannot be blank")
  @Schema(example = "Quận 9")
  private String district;

  @NotNull(message = "Province is required")
  @NotBlank(message = "Province cannot be blank")
  @Schema(example = "Thành phố Thủ Đức")
  private String province;
}

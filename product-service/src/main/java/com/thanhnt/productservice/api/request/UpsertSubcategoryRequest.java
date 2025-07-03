package com.thanhnt.productservice.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpsertSubcategoryRequest {
  private Long id;

  @NotNull(message = "Name must not be null")
  @NotBlank(message = "Name must not be blank")
  private String name;

  @NotNull(message = "Description must not be null")
  @NotBlank(message = "Description must not be blank")
  private String description;

  private RequestType requestType;

  @NotNull(message = "Category id must not be null")
  private Long categoryId;
}

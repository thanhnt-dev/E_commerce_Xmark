package com.thanhnt.userservice.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {
  private final String errorCode;
  private final String message;
}

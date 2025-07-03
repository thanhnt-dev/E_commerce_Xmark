package com.thanhnt.orderservice.infrastructure.rest.interceptor;

import com.thanhnt.orderservice.api.response.BaseResponse;
import com.thanhnt.orderservice.api.response.ExceptionResponse;
import com.thanhnt.orderservice.application.exception.AppException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<BaseResponse<List<ExceptionResponse>>>
      handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ExceptionResponse> responses =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ExceptionResponse(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

    BaseResponse<List<ExceptionResponse>> response = BaseResponse.build(responses, false);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {AppException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleAppException(
      AppException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }
}

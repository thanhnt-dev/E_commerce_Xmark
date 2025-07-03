package com.thanhnt.userservice.api.facade;

import com.thanhnt.userservice.api.request.*;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.LoginResponse;
import com.thanhnt.userservice.api.response.SignUpResponse;
import com.thanhnt.userservice.application.dto.ValidateTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AccountFacade {
  BaseResponse<LoginResponse> login(LoginRequest request);

  BaseResponse<LoginResponse> loginWith2Fa(ConfirmOTPWithLogin2FARequest request);

  BaseResponse<SignUpResponse> signUp(SignUpRequest request);

  BaseResponse<Void> logout(HttpServletRequest request);

  BaseResponse<Void> deleteAccount();

  BaseResponse<Void> confirmOTP(ConfirmOTPRequest request);

  BaseResponse<Void> resendOTP(ResendOTPRequest request);

  BaseResponse<Void> changePassword(ChangePasswordRequest request);

  BaseResponse<Void> forgotPassword(ForgotPasswordRequest request);

  BaseResponse<Void> resetPassword(ResetPasswordRequest request);

  BaseResponse<Void> toggleTwoFactorAuthentication();

  BaseResponse<Void> confirmTwoFactorAuthentication(Confirm2FARequest request);

  ResponseEntity<ValidateTokenDTO> validateToken(String token);
}

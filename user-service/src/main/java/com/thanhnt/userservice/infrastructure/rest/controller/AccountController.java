package com.thanhnt.userservice.infrastructure.rest.controller;

import com.thanhnt.userservice.api.facade.AccountFacade;
import com.thanhnt.userservice.api.request.*;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.LoginResponse;
import com.thanhnt.userservice.api.response.SignUpResponse;
import com.thanhnt.userservice.application.dto.ValidateTokenDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
  private final AccountFacade accountFacade;

  @Value("${thanhnt.urlAuthorizationLoginGoogle}")
  private String GOOGLE_LOGIN_URL;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Login account",
      tags = {"Account APIs"})
  public BaseResponse<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
    return this.accountFacade.login(request);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Logout account",
      tags = {"Account APIs"})
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public BaseResponse<Void> logout(HttpServletRequest request) {
    return this.accountFacade.logout(request);
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Create account with email and phone",
      tags = {"Account APIs"})
  public BaseResponse<SignUpResponse> signUp(@Validated @RequestBody SignUpRequest request) {
    return this.accountFacade.signUp(request);
  }

  @PostMapping("/confirm-otp")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Confirm code OTP",
      tags = {"Account APIs"})
  public BaseResponse<Void> confirmOTP(@Validated @RequestBody ConfirmOTPRequest request) {
    return this.accountFacade.confirmOTP(request);
  }

  @PostMapping("/resend-otp")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Resend code OTP",
      tags = {"Account APIs"})
  public BaseResponse<Void> resendOTP(@Validated @RequestBody ResendOTPRequest request) {
    return this.accountFacade.resendOTP(request);
  }

  @PutMapping("/change-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Change password",
      tags = {"Account APIs"})
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public BaseResponse<Void> changePassword(@Validated @RequestBody ChangePasswordRequest request) {
    return this.accountFacade.changePassword(request);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Delete account",
      tags = {"Account APIs"})
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public BaseResponse<Void> deleteAccount() {
    return this.accountFacade.deleteAccount();
  }

  @PostMapping("/forgot-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Forgot account",
      tags = {"Account APIs"})
  public BaseResponse<Void> forgotPassword(@Validated @RequestBody ForgotPasswordRequest request) {
    return this.accountFacade.forgotPassword(request);
  }

  @PostMapping("/reset-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Forgot account",
      tags = {"Account APIs"})
  public BaseResponse<Void> resetPassword(@Validated @RequestBody ResetPasswordRequest request) {
    return this.accountFacade.resetPassword(request);
  }

  @PutMapping("/2fa/toggle")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Toggle 2FA Authentication",
      tags = {"Account APIs"})
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public BaseResponse<Void> toggleTwoFactorAuthentication() {
    return this.accountFacade.toggleTwoFactorAuthentication();
  }

  @PutMapping("/2fa/confirm")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Confirm 2FA Authentication",
      tags = {"Account APIs"})
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  public BaseResponse<Void> confirmTwoFactorAuthentication(
      @Validated @RequestBody Confirm2FARequest request) {
    return this.accountFacade.confirmTwoFactorAuthentication(request);
  }

  @PostMapping("/login/2fa")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Confirm 2FA Authentication Login",
      tags = {"Account APIs"})
  public BaseResponse<LoginResponse> confirmOtpLogin(
      @RequestBody ConfirmOTPWithLogin2FARequest request) {
    return this.accountFacade.loginWith2Fa(request);
  }

  @GetMapping("/login/google")
  @Operation(
      summary = "Login with Google",
      tags = {"Account APIs"})
  public void loginWithGoogle(HttpServletResponse response) throws IOException {
    response.sendRedirect(GOOGLE_LOGIN_URL);
  }

  @Hidden
  @GetMapping(value = "/validate-token", headers = "API_SECRET_HEADER=secret1403")
  @Operation(
      summary = "Login with Google",
      tags = {"Account APIs"})
  public ResponseEntity<ValidateTokenDTO> validateToken(
      @RequestHeader("Authorization") String token) {
    return accountFacade.validateToken(token);
  }
}

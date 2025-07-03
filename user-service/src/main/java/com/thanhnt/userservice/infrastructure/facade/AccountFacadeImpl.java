package com.thanhnt.userservice.infrastructure.facade;

import com.thanhnt.userservice.api.facade.AccountFacade;
import com.thanhnt.userservice.api.request.*;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.LoginResponse;
import com.thanhnt.userservice.api.response.SignUpResponse;
import com.thanhnt.userservice.api.response.TokenResponse;
import com.thanhnt.userservice.application.dto.OTPMailDTO;
import com.thanhnt.userservice.application.dto.UserSnapshotDTO;
import com.thanhnt.userservice.application.dto.ValidateTokenDTO;
import com.thanhnt.userservice.application.exception.ChangePasswordException;
import com.thanhnt.userservice.application.exception.LoginException;
import com.thanhnt.userservice.application.exception.OTPException;
import com.thanhnt.userservice.application.exception.TokenException;
import com.thanhnt.userservice.application.service.*;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import com.thanhnt.userservice.domain.entity.commons.Gender;
import com.thanhnt.userservice.domain.entity.commons.OTPType;
import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import com.thanhnt.userservice.domain.entity.token.RefreshToken;
import com.thanhnt.userservice.domain.entity.users.UserProvider;
import com.thanhnt.userservice.domain.entity.users.Users;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountFacadeImpl implements AccountFacade {
  private final AccountService accountService;
  private final TokenService tokenService;
  private final JwtTokenService jwtTokenService;
  private final RoleService roleService;
  private final CacheService cacheService;
  private final CloudinaryService cloudinaryService;
  private final UserMailQueueProducer userMailQueueProducer;
  private final PasswordEncoder passwordEncoder;
  private final String REDIS_KEY_2FA = "LOGIN_2FA";

  @Value("${thanhnt.userCacheKey}")
  private String userKeyCachePrefix;

  private final AuthenticationManager authenticationManager;

  @Override
  public BaseResponse<LoginResponse> login(LoginRequest request) {
    log.info("Login request: {}", request.toString());
    var principle = accountService.loadUserByUsername(request.getEmail());
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    } catch (BadCredentialsException e) {
      throw new LoginException(ErrorCode.BAD_CREDENTIAL_LOGIN);
    }

    Authentication authentication = setAuthentication(principle);
    SecurityUserDetail securityUserDetail = (SecurityUserDetail) authentication.getPrincipal();
    boolean isDeactivate = !securityUserDetail.getIsActive();
    if (isDeactivate) throw new LoginException(ErrorCode.ACCOUNT_IS_DEACTIVATED);

    if (securityUserDetail.getIsFirstLogin()) {
      sendOtp(securityUserDetail.getEmail(), OTPType.REGISTER);
      log.info(
          "User {} is first login, sending OTP for verification ", securityUserDetail.getEmail());
      return BaseResponse.build(
          LoginResponse.builder().message(ErrorCode.ACCOUNT_NON_VERIFY.getMessage()).build(),
          false);
    }

    if (securityUserDetail.getIsTwoFactor()) {
      sendOtp(securityUserDetail.getEmail(), OTPType.TWO_FA);
      String token = generateToken2Fa(securityUserDetail.getEmail());
      return BaseResponse.build(LoginResponse.builder().message(token).build(), false);
    }

    return BaseResponse.build(buildLoginResponse(securityUserDetail), true);
  }

  @Override
  public BaseResponse<LoginResponse> loginWith2Fa(ConfirmOTPWithLogin2FARequest request) {
    log.info("Login with 2FA request: {}", request.toString());
    String email = new String(Base64.getDecoder().decode(request.getToken()));
    validateToken2Fa(email, request.getToken());
    validateOTPCode(email, request.getType().name(), request.getOtp());

    var principle = accountService.loadUserByUsername(email);
    Authentication authentication = setAuthentication(principle);

    SecurityUserDetail securityUserDetail = (SecurityUserDetail) authentication.getPrincipal();
    return BaseResponse.build(buildLoginResponse(securityUserDetail), true);
  }

  @Override
  public BaseResponse<Void> logout(HttpServletRequest request) {
    log.info("Logout request received");
    var authentication =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var user = accountService.findByEmail(authentication.getEmail());
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth == null) {
      throw new RuntimeException("Authorization header is missing");
    }
    String token = headerAuth.substring(7);
    log.info("User {} is logging out with token: {}", user.getEmail(), token);
    Long expirationDate = jwtTokenService.getExpirationDateFromJwtToken(token);
    Long timestamp = Instant.now().getEpochSecond();
    if (expirationDate < timestamp) {
      return BaseResponse.ok();
    }
    int minutes = (int) (expirationDate - timestamp) / 60 + 1;
    String REDIS_KEY_LOGOUT = "LOGOUT";
    String redisKey = String.format("%s-%s", REDIS_KEY_LOGOUT, token);
    cacheService.store(redisKey, token, minutes, TimeUnit.MINUTES);
    tokenService.deleteRefreshToken(user.getId());
    log.info(
        "User {} logged out successfully, token stored in cache with key: {}",
        user.getEmail(),
        redisKey);
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> deleteAccount() {
    var authentication =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.info("Delete account request for user: {}", authentication.getEmail());
    var user = accountService.findByEmail(authentication.getEmail());
    user.deactivate();
    accountService.updateUser(user);
    return BaseResponse.ok();
  }

  private LoginResponse buildLoginResponse(SecurityUserDetail principle) {
    List<RolesEnum> roleUsers =
        principle.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(RolesEnum::valueOf)
            .toList();
    log.info("Building login response for user: {}", principle.getEmail());
    TokenResponse token = jwtTokenService.generateToken(principle);
    Users user = accountService.findByEmail(principle.getEmail());
    RefreshToken refreshToken =
        RefreshToken.builder().refreshToken(token.getRefreshToken()).user(user).build();
    tokenService.saveRefreshToken(refreshToken);
    String avatar =
        user.getAvatar() == null ? "" : this.cloudinaryService.getImageUrl(user.getAvatar());
    log.info("User {} logged in successfully, roles: {}", principle.getEmail(), roleUsers);
    return LoginResponse.builder()
        .id(principle.getId())
        .phone(principle.getPhone())
        .avatar(avatar)
        .email(principle.getEmail())
        .firstName(principle.getFirstName())
        .provider(principle.getProvider())
        .lastName(principle.getLastName())
        .accessToken(token.getAccessToken())
        .refreshToken(token.getRefreshToken())
        .roles(roleUsers)
        .build();
  }

  @Override
  public BaseResponse<SignUpResponse> signUp(SignUpRequest request) {
    log.info("Sign up request: {}", request.toString());
    accountService.validateSignUp(request);

    var user =
        Users.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getEmail().substring(0, request.getEmail().indexOf("@")))
            .lastName(request.getEmail().substring(0, request.getEmail().indexOf("@")))
            .gender(Gender.MALE)
            .userProvider(UserProvider.NORMAL)
            .phone(request.getPhone())
            .dateOfBirth(0L)
            .build();

    Roles userRole = roleService.findRole(RolesEnum.ROLE_USER);
    user.addRole(userRole);
    Users users = accountService.signUp(user);
    sendOtp(request.getEmail(), OTPType.REGISTER);
    cacheService.storeUser(
        userKeyCachePrefix + user.getId(),
        UserSnapshotDTO.builder()
            .id(users.getId())
            .phone(users.getPhone())
            .fullName(users.getFirstName() + ' ' + users.getLastName())
            .avatar(users.getAvatar())
            .email(users.getEmail())
            .build());
    log.info("User {} signed up successfully", request.getEmail());
    return BaseResponse.build(SignUpResponse.builder().email(request.getEmail()).build(), true);
  }

  @Override
  public BaseResponse<Void> confirmOTP(ConfirmOTPRequest request) {
    log.info("Confirm OTP request: {}", request.toString());
    validateOTPCode(request.getEmail(), request.getType().name(), request.getOtp());
    if (request.getType().isRegister()) {
      Users users = accountService.findByEmail(request.getEmail());
      users.loggedIn();
      accountService.updateUser(users);
      log.info("User {} confirmed registration OTP successfully", request.getEmail());
    }
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> resendOTP(ResendOTPRequest request) {
    log.info("Resend OTP request: {}", request.toString());
    String key = String.format("%s-%s", request.getOtpType().name(), request.getEmail());
    Boolean hasKey = cacheService.hasKey(key);
    if (hasKey) cacheService.remove(key);

    sendOtp(request.getEmail(), request.getOtpType());
    log.info("OTP code resent to user: {}", request.getEmail());
    return BaseResponse.ok();
  }

  @Transactional
  @Override
  public BaseResponse<Void> changePassword(ChangePasswordRequest request) {
    log.info("Change password request: {}", request.toString());
    var authentication =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    boolean isNormalUserProvider = authentication.getProvider().isNormal();
    if (isNormalUserProvider) {
      boolean isCurrentPasswordDoesNotMatch =
          !passwordEncoder.matches(request.getOldPassword(), authentication.getPassword());
      if (isCurrentPasswordDoesNotMatch)
        throw new ChangePasswordException(ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH);
    }

    boolean isNewPasswordNotValid = !request.getNewPassword().equals(request.getConfirmPassword());
    if (isNewPasswordNotValid)
      throw new ChangePasswordException(ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH);

    Users user = accountService.findByEmail(authentication.getEmail());
    user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    accountService.updateUser(user);
    log.info("User {} changed password successfully", authentication.getEmail());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> forgotPassword(ForgotPasswordRequest request) {
    log.info("Forgot password request: {}", request.toString());
    Users users = accountService.findByEmail(request.getEmail());
    Logger.getGlobal().info("User: " + users);
    sendOtp(users.getEmail(), OTPType.FORGOT_PASSWORD);
    log.info("OTP code sent to user: {}", users.getEmail());
    return BaseResponse.ok();
  }

  @Override
  @Transactional
  public BaseResponse<Void> resetPassword(ResetPasswordRequest request) {
    log.info("Reset password request: {}", request.toString());
    Users users = accountService.findByEmail(request.getEmail());
    boolean isPasswordMismatch = !request.getNewPassword().equals(request.getConfirmPassword());
    if (isPasswordMismatch)
      throw new ChangePasswordException(ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH);

    users.changePassword(passwordEncoder.encode(request.getNewPassword()));
    accountService.updateUser(users);
    log.info("User {} reset password successfully", request.getEmail());
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<Void> toggleTwoFactorAuthentication() {
    log.info("Toggle two-factor authentication request");
    var authentication =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    sendOtp(authentication.getEmail(), OTPType.TWO_FA);

    return BaseResponse.ok();
  }

  @Override
  @Transactional
  public BaseResponse<Void> confirmTwoFactorAuthentication(Confirm2FARequest request) {
    log.info("Confirm two-factor authentication request: {}", request.toString());
    var authentication =
        (SecurityUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Users users = accountService.findByEmail(authentication.getEmail());
    users.toggle2FA(request.isTwoFA());
    accountService.updateUser(users);
    return BaseResponse.ok();
  }

  @Override
  public ResponseEntity<ValidateTokenDTO> validateToken(String token) {
    log.info("Validating token: {}", token);
    boolean isNotValidToken = !jwtTokenService.validateToken(token.substring(7));

    if (isNotValidToken) return ResponseEntity.badRequest().build();

    String email = jwtTokenService.getEmailFromJwtToken(token.substring(7));
    Users user = accountService.findByEmail(email);
    List<RolesEnum> roles = user.getRoles().stream().map(Roles::getRoleName).toList();

    var validateToken =
        ValidateTokenDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .phone(user.getPhone())
            .roles(roles)
            .build();
    return ResponseEntity.ok(validateToken);
  }

  private String generateOtp() {
    Random random = new Random();
    int otp = random.nextInt(999999);
    return String.format("%06d", otp);
  }

  private void sendOtp(String receiverMail, OTPType otpType) {
    log.info("Sending OTP to {} for type {}", receiverMail, otpType);
    String otpCode = generateOtp();
    String key = String.format("%s-%s", otpType.name(), receiverMail);
    cacheService.store(key, otpCode, 5, TimeUnit.MINUTES);
    log.info("OTP code {} stored in cache with key {}", otpCode, key);
    userMailQueueProducer.sendMailMessage(
        OTPMailDTO.builder().otpCode(otpCode).receiverMail(receiverMail).type(otpType).build());
  }

  private String generateToken2Fa(String email) {
    log.info("Generating 2FA token for email: {}", email);
    String token = Base64.getEncoder().encodeToString(email.getBytes());
    String key = String.format("%s-%s", REDIS_KEY_2FA, email);
    cacheService.store(key, token, 5, TimeUnit.MINUTES);
    log.info("2FA token {} stored in cache with key {}", token, key);
    return token;
  }

  private void validateToken2Fa(String email, String token) {
    log.info("Validating 2FA token for email: {}", email);
    String key = String.format("%s-%s", REDIS_KEY_2FA, email);
    String token2Fa = (String) cacheService.retrieve(key);
    if (token2Fa == null || token2Fa.isEmpty())
      throw new TokenException(ErrorCode.TOKEN_INVALID_OR_EXPIRED);
    boolean isNotMatchToken = !token.equals(token2Fa);
    if (isNotMatchToken) throw new TokenException(ErrorCode.TOKEN_DOES_NOT_MATCH);
    cacheService.remove(key);
  }

  private void validateOTPCode(String email, String otpType, String otpCode) {
    log.info("Validating OTP code for email: {}, type: {}", email, otpType);
    String key = String.format("%s-%s", otpType, email);

    String cacheOTP = (String) cacheService.retrieve(key);

    if (cacheOTP == null) throw new OTPException(ErrorCode.OTP_INVALID_OR_EXPIRED);
    if (!otpCode.equals(cacheOTP)) throw new OTPException(ErrorCode.OTP_DOES_NOT_MATCH);
    cacheService.remove(key);
    log.info("OTP code {} for email {} validated successfully", otpCode, email);
  }

  private Authentication setAuthentication(UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }
}

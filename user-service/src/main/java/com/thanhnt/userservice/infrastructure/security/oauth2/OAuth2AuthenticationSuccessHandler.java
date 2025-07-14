package com.thanhnt.userservice.infrastructure.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.userservice.api.response.TokenResponse;
import com.thanhnt.userservice.application.service.AccountService;
import com.thanhnt.userservice.application.service.CloudinaryService;
import com.thanhnt.userservice.application.service.JwtTokenService;
import com.thanhnt.userservice.application.service.RoleService;
import com.thanhnt.userservice.domain.entity.commons.Gender;
import com.thanhnt.userservice.domain.entity.role.Roles;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import com.thanhnt.userservice.domain.entity.users.UserProvider;
import com.thanhnt.userservice.domain.entity.users.Users;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final AccountService accountService;
  private final JwtTokenService jwtTokenService;
  private final CloudinaryService cloudinaryService;
  private final RoleService roleService;
  private final RestTemplate restTemplate;
  private static final Long DEFAULT_DATE_OF_BIRTH = 631152000000L;

  @Value("${thanhnt.defaultAvatar}")
  private String defaultAvatarGoogle;

  @Override
  @Transactional
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = (String) oAuth2User.getAttributes().get("email");
    UserDetails existingUser = accountService.getUserDetailByEmail(email);

    if (existingUser != null) {
      authenticateAndGenerateToken(response, existingUser);
      return;
    }
    String firstName =
        oAuth2User.getAttributes().get("given_name") != null
            ? (String) oAuth2User.getAttributes().get("given_name")
            : "";
    String lastName =
        oAuth2User.getAttributes().get("family_name") != null
            ? (String) oAuth2User.getAttributes().get("family_name")
            : "";
    String avatar = (String) oAuth2User.getAttributes().get("picture");
    Map<String, String> defaultAvatar = new HashMap<>();
    if (avatar != null) {
      byte[] imageBytes = restTemplate.getForObject(avatar, byte[].class);
      defaultAvatar = this.cloudinaryService.uploadImage(imageBytes);
    }
    var newUser =
        Users.builder()
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .avatar(
                defaultAvatar.get("asset_id") != null
                    ? defaultAvatar.get("asset_id")
                    : defaultAvatarGoogle)
            .publicId(defaultAvatar.get("public_id"))
            .userProvider(UserProvider.GOOGLE)
            .isFirstLogin(false)
            .gender(Gender.UNKNOWN)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .build();
    Roles roles = roleService.findRole(RolesEnum.ROLE_USER);
    newUser.addRole(roles);
    accountService.signUp(newUser);
    authenticateAndGenerateToken(response, accountService.loadUserByUsername(email));
  }

  private void authenticateAndGenerateToken(HttpServletResponse response, UserDetails userDetails)
      throws IOException {

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    SecurityUserDetail securityUserDetail = (SecurityUserDetail) authentication.getPrincipal();

    TokenResponse token = jwtTokenService.generateToken(securityUserDetail);
    Users user = accountService.findByEmail(securityUserDetail.getEmail());

    List<RolesEnum> roles =
        securityUserDetail.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(RolesEnum::valueOf)
            .toList();

    String avatar = user.getAvatar();

    String redirectUrl =
        String.format(
            "https://xmark.online/oauth2/redirect"
                + "?id=%s"
                + "&email=%s"
                + "&firstName=%s"
                + "&lastName=%s"
                + "&avatar=%s"
                + "&phone=%s"
                + "&provider=%s"
                + "&accessToken=%s"
                + "&refreshToken=%s"
                + "&roles=%s"
                + "&message=%s",
            URLEncoder.encode(user.getId().toString(), StandardCharsets.UTF_8),
            URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8),
            URLEncoder.encode(user.getFirstName(), StandardCharsets.UTF_8),
            URLEncoder.encode(user.getLastName(), StandardCharsets.UTF_8),
            URLEncoder.encode(avatar, StandardCharsets.UTF_8),
            URLEncoder.encode(
                user.getPhone() != null ? user.getPhone() : "", StandardCharsets.UTF_8),
            URLEncoder.encode(user.getUserProvider().name(), StandardCharsets.UTF_8),
            URLEncoder.encode(token.getAccessToken(), StandardCharsets.UTF_8),
            URLEncoder.encode(token.getRefreshToken(), StandardCharsets.UTF_8),
            URLEncoder.encode(new ObjectMapper().writeValueAsString(roles), StandardCharsets.UTF_8),
            URLEncoder.encode("Login success", StandardCharsets.UTF_8));

    response.sendRedirect(redirectUrl);
  }
}

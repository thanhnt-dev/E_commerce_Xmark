package com.thanhnt.userservice.infrastructure.rest.interceptor;

import com.thanhnt.userservice.application.exception.TokenException;
import com.thanhnt.userservice.application.service.AccountService;
import com.thanhnt.userservice.application.service.CacheService;
import com.thanhnt.userservice.application.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
  private final AccountService accountService;
  private final JwtTokenService jwtTokenService;
  private final CacheService cacheService;
  private final List<String> PUBLIC_URLS =
      List.of(
          "/api/v1/account/signup",
          "/api/v1/account/login/oauth2/code/google",
          "/api/v1/account/reset-password",
          "/api/v1/account/login",
          "/actuator/health");
  private final List<String> INTERNAL_API =
      List.of("api/v1/**", "/api/v1/user/permission", "/api/v1/users/**");
  private final String REDIS_KEY_LOGOUT = "LOGOUT";

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = getTokenFromHeader(request);
    String requestUri = request.getRequestURI();
    log.info("Request URI: {}", requestUri);
    var isPublic = PUBLIC_URLS.stream().anyMatch(requestUri::contains);
    var isInternal = INTERNAL_API.stream().anyMatch(requestUri::contains);

    if (isPublic || isInternal) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      String keyRedis = String.format("%s-%s", REDIS_KEY_LOGOUT, token);
      log.info("Checking token in Redis with key: {}", keyRedis);
      Boolean isExistToken = cacheService.hasKey(keyRedis);
      if (isExistToken) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      if (jwtTokenService.validateToken(token)) {
        String mail = jwtTokenService.getEmailFromJwtToken(token);
        var principle = accountService.loadUserByUsername(mail);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(principle, null, principle.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
      log.info("Token is valid, proceeding with filter chain.");
    } catch (TokenException exception) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  private String getTokenFromHeader(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (headerAuth != null) {
      return headerAuth.substring(7);
    }
    return null;
  }
}

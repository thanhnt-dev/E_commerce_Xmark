package com.thanhnt.productservice.infrastructure.rest.interceptor;

import com.thanhnt.productservice.application.dto.ValidateTokenDTO;
import com.thanhnt.productservice.application.service.UserServiceFeign;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

  private final UserServiceFeign userServiceFeign;

  private final List<String> PUBLIC_URLS =
      List.of("/swagger-ui/", "/v3/api-docs/", "/v3/api-docs/product-service", "/actuator/health");
  private final List<String> INTERNAL_API =
      List.of("/api/v1/products/validate", "/api/v1/products/update_quantity");

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String requestUri = request.getRequestURI();
    log.info("Request URI: {}", requestUri);
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
      log.info("Authorization header: {}", authHeader);
    } else {
      log.info("No Authorization header found");
    }
    var isPublic = PUBLIC_URLS.stream().anyMatch(requestUri::contains);
    var isInternal = INTERNAL_API.stream().anyMatch(requestUri::equals);

    if (isPublic || isInternal) {
      log.info(
          "Public or internal API request, skipping authentication filter for URI: {}", requestUri);
      filterChain.doFilter(request, response);
      return;
    }

    boolean isGetMethod = request.getMethod().equals("GET");
    boolean isPublicProductEndpoint =
        requestUri.equalsIgnoreCase("/api/v1/products")
            || requestUri.matches("/api/v1/products/\\d+");
    boolean isNotAuthenticated =
        request.getHeader("Authorization") == null || request.getHeader("Authorization").isEmpty();
    if (isNotAuthenticated && isPublicProductEndpoint && isGetMethod) {
      log.info(
          "Public product endpoint accessed without authentication, allowing request: {}",
          requestUri);
      filterChain.doFilter(request, response);
      return;
    }

    try {
      ValidateTokenDTO validateTokenDTO =
          userServiceFeign.validateToken(request.getHeader("Authorization"));
      log.info("Validate token response: {}", validateTokenDTO);
      if (validateTokenDTO != null) {
        List<GrantedAuthority> authorityList =
            validateTokenDTO.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(validateTokenDTO, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Authentication successful for user: {}", validateTokenDTO.getEmail());
      }
    } catch (Exception e) {
      log.error("Error validating token: {}", e.getMessage(), e);
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    filterChain.doFilter(request, response);
  }
}

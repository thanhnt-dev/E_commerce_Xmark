package com.thanhnt.messageservice.infrastructure.rest.interceptor;

import com.thanhnt.messageservice.application.dto.ValidateTokenDTO;
import com.thanhnt.messageservice.application.service.UserServiceFeign;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
  private final UserServiceFeign userServiceFeign;

  private final List<String> PUBLIC_URLS =
      List.of("/swagger-ui/", "/v3/api-docs/", "/v3/api-docs/message-service", "/actuator/health");
  private final List<String> INTERNAL_API = List.of("/api/v1/var");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestUri = request.getRequestURI();
    var isPublic = PUBLIC_URLS.stream().anyMatch(requestUri::contains);
    var isInternal = INTERNAL_API.stream().anyMatch(requestUri::contains);

    if (isPublic || isInternal) {
      filterChain.doFilter(request, response);
      return;
    }

    boolean isGetMethod = request.getMethod().equals("GET");
    boolean isNotAuthenticated =
        request.getHeader("Authorization") == null || request.getHeader("Authorization").isEmpty();
    if (isNotAuthenticated && isGetMethod) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      ValidateTokenDTO validateTokenDTO =
          userServiceFeign.validateToken(request.getHeader("Authorization"));

      if (validateTokenDTO != null) {
        List<GrantedAuthority> authorityList =
            validateTokenDTO.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(validateTokenDTO, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    filterChain.doFilter(request, response);
  }
}

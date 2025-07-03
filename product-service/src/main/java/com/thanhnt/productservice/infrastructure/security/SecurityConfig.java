package com.thanhnt.productservice.infrastructure.security;

import com.thanhnt.productservice.application.service.UserServiceFeign;
import com.thanhnt.productservice.infrastructure.rest.interceptor.AuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserServiceFeign userServiceFeign;
  private static final String[] WHITE_LISTS = {
    "/**",
    "/api/v1/**",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/api/v1/products/**",
    "/v3/api-docs/product-service",
    "/actuator/health"
  };

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public AuthenticationTokenFilter authenticationTokenFilter() {
    return new AuthenticationTokenFilter(userServiceFeign);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            request ->
                request.requestMatchers(WHITE_LISTS).permitAll().anyRequest().authenticated());
    http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

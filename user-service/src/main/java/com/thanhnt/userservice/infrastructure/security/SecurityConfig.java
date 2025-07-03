package com.thanhnt.userservice.infrastructure.security;

import com.thanhnt.userservice.application.service.*;
import com.thanhnt.userservice.infrastructure.rest.interceptor.AuthenticationTokenFilter;
import com.thanhnt.userservice.infrastructure.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.thanhnt.userservice.infrastructure.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AccountService accountService;
  private final JwtTokenService jwtTokenService;
  private final CloudinaryService cloudinaryService;
  private final RestTemplate restTemplate;
  private final RoleService roleService;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
  private final CacheService cacheService;

  private static final String[] WHITE_LISTS = {
    "/api/v1/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/actuator/health"
  };

  @Value("${thanhnt.urlAuthorizationLoginGoogle}")
  private String GOOGLE_LOGIN_URL;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(accountService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public AuthenticationTokenFilter tokenFilter() {
    return new AuthenticationTokenFilter(accountService, jwtTokenService, cacheService);
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
    http.oauth2Login(
        oauth2 ->
            oauth2
                .loginPage(GOOGLE_LOGIN_URL)
                .permitAll()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler));
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
    return new OAuth2AuthenticationSuccessHandler(
        accountService, jwtTokenService, cloudinaryService, roleService, restTemplate);
  }
}

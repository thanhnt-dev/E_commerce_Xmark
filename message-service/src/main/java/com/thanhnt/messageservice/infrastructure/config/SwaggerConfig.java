package com.thanhnt.messageservice.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Ecommerce API"),
    servers = {@Server(url = "/message-service"), @Server(url = "/")})
@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization")
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi openApi() {
    String[] paths = {"/api/**", "/message-service/**"};
    return GroupedOpenApi.builder()
        .group("message-service")
        .packagesToScan("com.thanhnt.messageservice.infrastructure.rest.controller")
        .pathsToMatch(paths)
        .build();
  }
}

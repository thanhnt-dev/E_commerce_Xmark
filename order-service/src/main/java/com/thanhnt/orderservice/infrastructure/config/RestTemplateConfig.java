package com.thanhnt.orderservice.infrastructure.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Value("${service.external.timeout}")
  private long externalServiceTimeout;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setReadTimeout(Duration.ofMillis(externalServiceTimeout))
        .build();
  }
}

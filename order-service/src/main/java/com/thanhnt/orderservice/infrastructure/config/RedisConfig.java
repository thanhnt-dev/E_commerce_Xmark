package com.thanhnt.orderservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.orderservice.api.response.OrderDetailResponse;
import com.thanhnt.orderservice.application.dto.ShopSnapshotDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Value("${spring.data.redis-order.host}")
  private String redisOrderHost;

  @Value("${spring.data.redis-order.port}")
  private int redisOrderPort;

  @Value("${spring.data.redis-order.database}")
  private int redisOrderDatabase;

  @Value("${spring.data.redis-shop.host}")
  private String redisShopHost;

  @Value("${spring.data.redis-shop.port}")
  private int redisShopPort;

  @Value("${spring.data.redis-shop.database}")
  private int redisShopDatabase;

  @Bean(name = "redisOrderConnectionFactory")
  public RedisConnectionFactory redisOrderConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisOrderHost);
    configuration.setPort(redisOrderPort);
    configuration.setDatabase(redisOrderDatabase);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean(name = "redisShopConnectionFactory")
  public RedisConnectionFactory redisShopConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisShopHost);
    configuration.setPort(redisShopPort);
    configuration.setDatabase(redisShopDatabase);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean(name = "redisOrderTemplate")
  public RedisTemplate<String, OrderDetailResponse> redisOrderTemplate(
      @Qualifier("redisOrderConnectionFactory")
          RedisConnectionFactory redisOrderConnectionFactory) {
    RedisTemplate<String, OrderDetailResponse> template = new RedisTemplate<>();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Jackson2JsonRedisSerializer<OrderDetailResponse> serializer =
        new Jackson2JsonRedisSerializer<>(OrderDetailResponse.class);

    template.setConnectionFactory(redisOrderConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }

  @Bean(name = "redisShopTemplate")
  public RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate(
      @Qualifier("redisShopConnectionFactory") RedisConnectionFactory redisShopConnectionFactory) {
    RedisTemplate<String, ShopSnapshotDTO> template = new RedisTemplate<>();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Jackson2JsonRedisSerializer<ShopSnapshotDTO> serializer =
        new Jackson2JsonRedisSerializer<>(ShopSnapshotDTO.class);

    template.setConnectionFactory(redisShopConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }

  @Bean(name = "redisShopCacheManager")
  public RedisCacheManager redisShopCacheManager(
      @Qualifier("redisShopConnectionFactory") RedisConnectionFactory redisShopConnectionFactory) {
    return RedisCacheManager.create(redisShopConnectionFactory);
  }

  @Bean(name = "redisOrderCacheManager")
  public RedisCacheManager redisOrderCacheManager(
      @Qualifier("redisOrderConnectionFactory")
          RedisConnectionFactory redisOrderConnectionFactory) {
    return RedisCacheManager.create(redisOrderConnectionFactory);
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(
      @Qualifier("redisOrderConnectionFactory")
          RedisConnectionFactory redisOrderConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisOrderConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.afterPropertiesSet();
    return template;
  }
}

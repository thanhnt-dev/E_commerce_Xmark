package com.thanhnt.storeservice.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.storeservice.application.dto.ShopSnapshotDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Value("${spring.data.redis-user.host}")
  private String redisShopHost;

  @Value("${spring.data.redis-user.port}")
  private int redisShopPort;

  @Value("${spring.data.redis-user.database}")
  private int redisShopDatabase;

  @Bean(name = "redisShopConnectionFactory")
  public RedisConnectionFactory redisShopConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisShopHost);
    config.setPort(redisShopPort);
    config.setDatabase(redisShopDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisShopTemplate")
  public RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate(
      @Qualifier("redisShopConnectionFactory") RedisConnectionFactory redisShopConnectionFactory) {
    RedisTemplate<String, ShopSnapshotDTO> template = new RedisTemplate<>();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.activateDefaultTyping(
        objectMapper.getPolymorphicTypeValidator(),
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    objectMapper.deactivateDefaultTyping();

    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(objectMapper);

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
}

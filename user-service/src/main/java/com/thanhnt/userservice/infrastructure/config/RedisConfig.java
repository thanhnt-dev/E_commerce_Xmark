package com.thanhnt.userservice.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.userservice.application.dto.UserSnapshotDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.database}")
  private int redisDatabase;

  @Value("${spring.data.redis-user.host}")
  private String redisUserHost;

  @Value("${spring.data.redis-user.port}")
  private int redisUserPort;

  @Value("${spring.data.redis-user.database}")
  private int redisUserDatabase;

  @Bean
  @Primary
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisHost);
    config.setPort(redisPort);
    config.setDatabase(redisDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisUserConnectionFactory")
  public RedisConnectionFactory redisUserConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisUserHost);
    config.setPort(redisUserPort);
    config.setDatabase(redisUserDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean
  @Primary
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setDefaultSerializer(new StringRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new GenericToStringSerializer<>(String.class));
    template.setHashValueSerializer(new GenericToStringSerializer<>(String.class));
    return template;
  }

  @Bean(name = "redisUserTemplate")
  public RedisTemplate<String, UserSnapshotDTO> redisUserTemplate(
      @Qualifier("redisUserConnectionFactory") RedisConnectionFactory redisUserConnectionFactory) {
    RedisTemplate<String, UserSnapshotDTO> template = new RedisTemplate<>();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.activateDefaultTyping(
        objectMapper.getPolymorphicTypeValidator(),
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    objectMapper.deactivateDefaultTyping();

    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(objectMapper);

    template.setConnectionFactory(redisUserConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();

    return template;
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.create(connectionFactory);
  }

  @Bean(name = "redisUserCacheManager")
  public RedisCacheManager redisUserCacheManager(
      @Qualifier("redisUserConnectionFactory") RedisConnectionFactory redisUserConnectionFactory) {
    return RedisCacheManager.create(redisUserConnectionFactory);
  }
}

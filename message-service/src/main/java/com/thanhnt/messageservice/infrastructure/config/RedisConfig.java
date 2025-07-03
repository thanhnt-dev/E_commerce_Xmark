package com.thanhnt.messageservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.messageservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.messageservice.application.dto.UserSnapshotDTO;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis-user.host}")
  private String redisUserHost;

  @Value("${spring.data.redis-user.port}")
  private int redisUserPort;

  @Value("${spring.data.redis-user.database}")
  private int redisUserDatabase;

  //  @Value("${spring.data.redis-shop.host}")
  //  private String redisShopHost;
  //
  //  @Value("${spring.data.redis-shop.port}")
  //  private int redisShopPort;
  //
  //  @Value("${spring.data.redis-shop.database}")
  //  private int redisShopDatabase;

  @Bean(name = "redisUserConnectionFactory")
  @Primary
  public RedisConnectionFactory redisUserConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisUserHost);
    config.setPort(redisUserPort);
    config.setDatabase(redisUserDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisUserTemplate")
  public RedisTemplate<String, UserSnapshotDTO> redisUserSnapshotTemplate(
      @Qualifier("redisUserConnectionFactory") RedisConnectionFactory redisUserConnectionFactory) {
    RedisTemplate<String, UserSnapshotDTO> template = new RedisTemplate<>();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Jackson2JsonRedisSerializer<UserSnapshotDTO> serializer =
        new Jackson2JsonRedisSerializer<>(UserSnapshotDTO.class);

    template.setConnectionFactory(redisUserConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();

    return template;
  }

  @Bean(name = "redisShopConnectionFactory")
  public RedisConnectionFactory redisShopConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisUserHost);
    config.setPort(redisUserPort);
    config.setDatabase(redisUserDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisShopTemplate")
  public RedisTemplate<String, ShopSnapshotDTO> shopSnapshotTemplate(
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

  @Bean(name = "redisUserCacheManager")
  public RedisCacheManager redisUserCacheManager(
      @Qualifier("redisUserConnectionFactory") RedisConnectionFactory redisUserConnectionFactory) {
    return RedisCacheManager.create(redisUserConnectionFactory);
  }

  @Bean(name = "redisShopCacheManager")
  public RedisCacheManager redisShopCacheManager(
      @Qualifier("redisShopConnectionFactory") RedisConnectionFactory redisShopConnectionFactory) {
    return RedisCacheManager.create(redisShopConnectionFactory);
  }
}

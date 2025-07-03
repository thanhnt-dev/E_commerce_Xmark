package com.thanhnt.productservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhnt.productservice.application.dto.ShopSnapshotDTO;
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
  @Value("${spring.data.redis-user.host}")
  private String redisShopHost;

  @Value("${spring.data.redis-user.port}")
  private int redisShopPort;

  @Value("${spring.data.redis-user.database}")
  private int redisShopDatabase;

  @Value("${spring.data.redis-product.host}")
  private String redisProductHost;

  @Value("${spring.data.redis-product.port}")
  private int redisProductPort;

  @Value("${spring.data.redis-product.database}")
  private int redisProductDatabase;

  @Bean(name = "redisShopConnectionFactory")
  public RedisConnectionFactory redisShopConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisShopHost);
    configuration.setPort(redisShopPort);
    configuration.setDatabase(redisShopDatabase);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean(name = "redisProductConnectionFactory")
  public RedisConnectionFactory redisProductConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisProductHost);
    config.setPort(redisProductPort);
    config.setDatabase(redisProductDatabase);
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(
      @Qualifier("redisProductConnectionFactory")
          RedisConnectionFactory redisProductConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisProductConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
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

  @Bean(name = "redisProductTemplate")
  public RedisTemplate<String, Object> redisProductTemplate(
      @Qualifier("redisProductConnectionFactory")
          RedisConnectionFactory redisProductConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisProductConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.afterPropertiesSet();
    return template;
  }

  @Bean(name = "redisShopCacheManager")
  public RedisCacheManager redisShopCacheManager(
      @Qualifier("redisShopConnectionFactory") RedisConnectionFactory redisShopConnectionFactory) {
    return RedisCacheManager.create(redisShopConnectionFactory);
  }

  @Bean(name = "redisProductCacheManager")
  public RedisCacheManager cacheManager(
      @Qualifier("redisProductConnectionFactory")
          RedisConnectionFactory redisProductConnectionFactory) {
    return RedisCacheManager.create(redisProductConnectionFactory);
  }
}

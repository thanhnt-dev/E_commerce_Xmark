package com.thanhnt.messageservice.infrastructure.config;

import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.application.dto.NotificationDTO;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String chatGroupId;

  @Value("${spring.kafka.consumer.notification-group-id}")
  private String notificationGroupId;

  // Producer cho MessageDTO (chat)
  @Bean
  public ProducerFactory<String, MessageDTO> messageProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers);
    configProps.put(
        org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    configProps.put(
        org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, MessageDTO> messageKafkaTemplate() {
    return new KafkaTemplate<>(messageProducerFactory());
  }

  //  @Bean
  //  public ProducerFactory<String, NotificationDTO> notificationProducerFactory() {
  //    Map<String, Object> configProps = new HashMap<>();
  //    configProps.put(
  //        org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
  //        bootstrapServers);
  //    configProps.put(
  //        org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
  //        StringSerializer.class);
  //    configProps.put(
  //        org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
  //        JsonSerializer.class);
  //    return new DefaultKafkaProducerFactory<>(configProps);
  //  }

  //  @Bean
  //  public KafkaTemplate<String, NotificationDTO> notificationKafkaTemplate() {
  //    return new KafkaTemplate<>(notificationProducerFactory());
  //  }

  // Consumer cho MessageDTO (chat)
  @Bean
  public ConsumerFactory<String, MessageDTO> messageConsumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers);
    configProps.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, chatGroupId);
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        JsonDeserializer.class);
    configProps.put(
        JsonDeserializer.TRUSTED_PACKAGES, "com.thanhnt.messageservice.application.dto");
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    configProps.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
    configProps.put(
        org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, MessageDTO>
      messageKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(messageConsumerFactory());
    factory.getContainerProperties().setPollTimeout(3000);
    factory.setConcurrency(1);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, NotificationDTO> notificationConsumerFactory() {
    JsonDeserializer<NotificationDTO> deserializer = new JsonDeserializer<>(NotificationDTO.class);
    deserializer.addTrustedPackages("*");
    deserializer.setRemoveTypeHeaders(false);
    deserializer.setUseTypeMapperForKey(false);

    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, notificationGroupId);
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
    configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
    configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);

    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, NotificationDTO>
      notificationKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, NotificationDTO> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(notificationConsumerFactory());
    factory.getContainerProperties().setPollTimeout(3000);
    factory.setConcurrency(1);
    return factory;
  }
}

package com.thanhnt.productservice.infrastructure.config;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${rabbitmq.exchangeName}")
  private String exchange;

  @Value("${rabbitmq.product-elastic-queue}")
  private String productElasticQueue;

  @Value("${rabbitmq.product-elastic-routing-key}")
  private String productElasticRoutingKey;

  @Value("${rabbitmq.product-log-queue}")
  private String productLogQueue;

  @Value("${rabbitmq.product-log-routing-key}")
  private String productLogRoutingKey;

  @Bean
  public Queue productElasticQueue() {
    return new Queue(productElasticQueue);
  }

  @Bean
  public Queue productLogQueue() {
    return new Queue(productLogQueue);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  public Binding productElasticBinding() {
    return BindingBuilder.bind(productElasticQueue()).to(exchange()).with(productElasticRoutingKey);
  }

  @Bean
  public Binding productLogBinding() {
    return BindingBuilder.bind(productLogQueue()).to(exchange()).with(productLogRoutingKey);
  }

  //    @Bean
  //    public MessageConverter converter() {
  //        return new Jackson2JsonMessageConverter();
  //    }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter converter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter);
    return rabbitTemplate;
  }

  @Bean
  public MessageConverter converter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper
        .getFactory()
        .setStreamWriteConstraints(StreamWriteConstraints.builder().maxNestingDepth(2000).build());

    return objectMapper;
  }
}

package com.thanhnt.mailservice.global.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${rabbitmq.user-mail-queue}")
  private String userQueue;

  @Value("${rabbitmq.store-mail-routing-key}")
  private String storeQueue;

  @Value("${rabbitmq.exchangeName}")
  private String exchange;

  @Value("${rabbitmq.user-mail-routing-key}")
  private String userRoutingKey;

  @Value("${rabbitmq.store-mail-routing-key}")
  private String storeRoutingKey;

  @Bean
  public Queue queue() {
    return new Queue(userQueue);
  }

  @Bean
  public Queue storeQueue() {
    return new Queue(storeQueue);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(queue()).to(exchange()).with(userRoutingKey);
  }

  @Bean
  public Binding storeBinding() {
    return BindingBuilder.bind(storeQueue()).to(exchange()).with(storeRoutingKey);
  }

  @Bean
  public MessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter());
    return rabbitTemplate;
  }
}

package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.dto.ProductElasticsearchDTO;
import com.thanhnt.productservice.application.dto.ProductLogDetailDTO;
import com.thanhnt.productservice.application.service.ProductQueueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueueProducerImpl implements ProductQueueProducer {

  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmq.exchangeName}")
  private String exchange;

  @Value("${rabbitmq.product-elastic-routing-key}")
  private String productElasticRoutingKey;

  @Value("${rabbitmq.product-log-routing-key}")
  private String productLogRoutingKey;

  @Override
  public void syncProductToElasticsearch(ProductElasticsearchDTO product) {
    log.info("Sending product to Elasticsearch: " + product.getId());
    rabbitTemplate.convertAndSend(exchange, productElasticRoutingKey, product);
  }

  @Override
  public void writeProductLogToMongoAndIncrementViewCount(ProductLogDetailDTO productLogDetailDTO) {
    log.info("Sending product log to MongoDB: " + productLogDetailDTO.getProductId());
    rabbitTemplate.convertAndSend(exchange, productLogRoutingKey, productLogDetailDTO);
  }
}

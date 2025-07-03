package com.thanhnt.productservice.infrastructure.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

  @Value("${spring.elasticsearch.host}")
  private String elasticsearchHost;

  @Value("${spring.elasticsearch.port}")
  private int elasticsearchPort;

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    RestClient restClient =
        RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort)).build();

    ElasticsearchTransport transport =
        new RestClientTransport(restClient, new JacksonJsonpMapper());

    return new ElasticsearchClient(transport);
  }
}

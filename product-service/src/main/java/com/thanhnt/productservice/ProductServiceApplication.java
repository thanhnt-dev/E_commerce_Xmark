package com.thanhnt.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories
@EnableScheduling
public class ProductServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }
}

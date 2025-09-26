package com.aquariux.fintech.trading.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    // TODO: RestClient configuration (e.g., timeouts)
    return RestClient.builder().build();
  }
}

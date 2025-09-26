package com.aquariux.fintech.trading.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trading")
@Data
public class TradingProperties {
  private List<String> pairs;
}

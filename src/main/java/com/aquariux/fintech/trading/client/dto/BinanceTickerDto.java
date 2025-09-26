package com.aquariux.fintech.trading.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BinanceTickerDto {
  private String symbol;

  @JsonProperty("bidPrice")
  private BigDecimal bid;

  @JsonProperty("askPrice")
  private BigDecimal ask;
}

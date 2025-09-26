package com.aquariux.fintech.trading.client.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class HuobiResponseDto {
  @Data
  public static class HuobiTickerDto {
    private String symbol;
    private BigDecimal bid;
    private BigDecimal ask;
  }

  private List<HuobiTickerDto> data;
}

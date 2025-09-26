package com.aquariux.fintech.trading.dto;

import com.aquariux.fintech.trading.entity.TradeSide;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class TradeResponse {
  private String symbol;

  private TradeSide side;

  private BigDecimal price; // best price at execution time

  private BigDecimal quantity;

  private BigDecimal quoteAmount; // total price = price * quantity

  private Instant executedAt;
}

package com.aquariux.fintech.trading.dto;

import com.aquariux.fintech.trading.entity.TradeSide;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TradeRequest {
  @NotBlank
  private String symbol;

  @NotNull
  private TradeSide side;

  @NotNull
  @DecimalMin("0.0001")
  private BigDecimal quantity;
}

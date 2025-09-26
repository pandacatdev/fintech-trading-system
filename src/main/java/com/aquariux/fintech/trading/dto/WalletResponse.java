package com.aquariux.fintech.trading.dto;

import com.aquariux.fintech.trading.entity.Asset;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponse {
  private Asset asset;
  private BigDecimal balance;
}
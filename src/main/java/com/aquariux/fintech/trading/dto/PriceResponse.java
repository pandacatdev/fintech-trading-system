package com.aquariux.fintech.trading.dto;

import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.Exchange;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponse {
  private Asset baseAsset;
  private Asset quoteAsset;
  private BigDecimal bestBid;
  private Exchange bestBidSource;
  private BigDecimal bestAsk;
  private Exchange bestAskSource;
  private Instant asOf;
}

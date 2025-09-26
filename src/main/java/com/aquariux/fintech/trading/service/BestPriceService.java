package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.repository.BestPriceRepository;
import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.BestPrice;
import com.aquariux.fintech.trading.entity.Exchange;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BestPriceService {
  private final BestPriceRepository bestPriceRepository;

  public BestPrice updateBestPrice(
      Asset base, Asset quote,
      BigDecimal bestBid, Exchange bestBidSource,
      BigDecimal bestAsk, Exchange bestAskSource) {

    BestPrice bestPrice = bestPriceRepository
        .findByBaseAssetAndQuoteAsset(base, quote)
        .orElse(BestPrice.builder()
            .baseAsset(base)
            .quoteAsset(quote)
            .build());

    bestPrice.setBestBid(bestBid);
    bestPrice.setBestBidSource(bestBidSource);
    bestPrice.setBestAsk(bestAsk);
    bestPrice.setBestAskSource(bestAskSource);

    return bestPriceRepository.save(bestPrice);
  }
}

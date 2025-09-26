package com.aquariux.fintech.trading.controller;

import com.aquariux.fintech.trading.dto.PriceResponse;
import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.BestPrice;
import com.aquariux.fintech.trading.service.BestPriceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
  private final BestPriceService bestPriceService;

  @GetMapping("/{base}/{quote}")
  public ResponseEntity<PriceResponse> getBestPrice(
      @PathVariable String base,
      @PathVariable String quote) {

    Asset baseAsset = Asset.valueOf(base.toUpperCase());
    Asset quoteAsset = Asset.valueOf(quote.toUpperCase());

    return bestPriceService.getBestPrice(baseAsset, quoteAsset)
        .map(this::mapToResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<PriceResponse> getAllBestPrices() {
    return bestPriceService.getAllBestPrices()
        .stream()
        .map(this::mapToResponse)
        .toList();
  }

  private PriceResponse mapToResponse(BestPrice bestPrice) {
    return PriceResponse.builder()
        .baseAsset(bestPrice.getBaseAsset())
        .quoteAsset(bestPrice.getQuoteAsset())
        .bestBid(bestPrice.getBestBid())
        .bestBidSource(bestPrice.getBestBidSource())
        .bestAsk(bestPrice.getBestAsk())
        .bestAskSource(bestPrice.getBestAskSource())
        .asOf(bestPrice.getAsOf())
        .build();
  }
}

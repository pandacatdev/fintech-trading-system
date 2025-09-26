package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.client.BinancePriceClient;
import com.aquariux.fintech.trading.client.HuobiPriceClient;
import com.aquariux.fintech.trading.client.dto.BinanceTickerDto;
import com.aquariux.fintech.trading.client.dto.HuobiResponseDto;
import com.aquariux.fintech.trading.config.TradingProperties;
import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.Exchange;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAggregationService {
  private final TradingProperties tradingProperties;

  private final BinancePriceClient binanceClient;
  private final HuobiPriceClient huobiClient;
  private final BestPriceService bestPriceService;

  @Scheduled(fixedRate = 10_000)
  public void fetchAndAggregate() {
    try {
      // fetch all prices for supported trading pairs
      CompletableFuture<Map<String, BinanceTickerDto>> binanceFuture =
          CompletableFuture.supplyAsync(() -> binanceClient.fetchPrices(tradingProperties.getPairs()));

      CompletableFuture<Map<String, HuobiResponseDto.HuobiTickerDto>> huobiFuture =
          CompletableFuture.supplyAsync(() -> huobiClient.fetchPrices(tradingProperties.getPairs()));

      Map<String, BinanceTickerDto> binanceMap = binanceFuture
          .exceptionally(ex -> Map.of())
          .get(3, TimeUnit.SECONDS);
      Map<String, HuobiResponseDto.HuobiTickerDto> huobiMap = huobiFuture
          .exceptionally(ex -> Map.of())
          .get(3, TimeUnit.SECONDS);

      // aggregate for each configured trading pair
      for (String symbol : tradingProperties.getPairs()) {
        Asset base = Asset.valueOf(symbol.replace("USDT", "")); // BTC, ETH
        Asset quote = Asset.USDT;

        BinanceTickerDto binanceQuote = binanceMap.get(symbol);
        HuobiResponseDto.HuobiTickerDto huobiQuote = huobiMap.get(symbol);

        if (binanceQuote == null && huobiQuote == null) {
          continue;
        }

        // for simplicity, skip bidQty/askQty, bidSize/askSize, only consider best bid/ask prices
        Pair<Exchange, BigDecimal> bestBidPair = getBestBid(binanceQuote, huobiQuote);
        Exchange bestBidSource = bestBidPair.getFirst();
        BigDecimal bestBid = bestBidPair.getSecond();

        Pair<Exchange, BigDecimal> bestAskPair = getBestAsk(binanceQuote, huobiQuote);
        Exchange bestAskSource = bestAskPair.getFirst();
        BigDecimal bestAsk = bestAskPair.getSecond();

        // Save or update best price in DB
        bestPriceService.updateBestPrice(base, quote, bestBid, bestBidSource, bestAsk, bestAskSource);
      }
    } catch (Exception e) {
      // TODO: structured logging + alerting
      System.err.println("Price aggregation failed: " + e.getMessage());
    }
  }


  private Pair<Exchange, BigDecimal> getBestBid(BinanceTickerDto binance, HuobiResponseDto.HuobiTickerDto huobi) {
    if (binance != null && huobi != null) {
      return binance.getBid().compareTo(huobi.getBid()) >= 0
          ? Pair.of(Exchange.BINANCE, binance.getBid())
          : Pair.of(Exchange.HUOBI, huobi.getBid());
    }

    if (binance != null) {
      return Pair.of(Exchange.BINANCE, binance.getBid());
    }

    return Pair.of(Exchange.HUOBI, huobi.getBid());
  }

  private Pair<Exchange, BigDecimal> getBestAsk(BinanceTickerDto binance, HuobiResponseDto.HuobiTickerDto huobi) {
    if (binance != null && huobi != null) {
      return binance.getAsk().compareTo(huobi.getAsk()) <= 0
          ? Pair.of(Exchange.BINANCE, binance.getAsk())
          : Pair.of(Exchange.HUOBI, huobi.getAsk());
    }

    if (binance != null) {
      return Pair.of(Exchange.BINANCE, binance.getAsk());
    }

    return Pair.of(Exchange.HUOBI, huobi.getAsk());
  }
}


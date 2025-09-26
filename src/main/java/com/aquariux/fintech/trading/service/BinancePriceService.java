package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.client.BinanceClient;
import com.aquariux.fintech.trading.client.dto.BinanceTickerDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class BinancePriceService {
  private final BinanceClient binanceClient;
  private static final Logger log = LoggerFactory.getLogger(BinancePriceService.class);

  public Map<String, BinanceTickerDto> fetchPrices(List<String> symbols) {
    log.info("Fetching Binance prices for symbols: {}", symbols);
    var binanceTickers = binanceClient.getBookTickers();
    log.debug("Received {} tickers from Binance", binanceTickers.length);
    return Arrays.stream(binanceTickers)
        .filter(t -> symbols.contains(t.getSymbol()))
        .collect(Collectors.toMap(BinanceTickerDto::getSymbol, t -> t));
  }
}

package com.aquariux.fintech.trading.client;

import com.aquariux.fintech.trading.client.dto.BinanceTickerDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BinancePriceClient {
  private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";

  private final RestClient restClient;

  public Map<String, BinanceTickerDto> fetchPrices(List<String> symbols) {
    var binanceTickers = restClient.get()
        .uri(BINANCE_URL)
        .retrieve()
        .body(BinanceTickerDto[].class);

    // TODO: Handle errors and nulls appropriately

    return Arrays.stream(binanceTickers)
        .filter(t -> symbols.contains(t.getSymbol()))
        .collect(Collectors.toMap(BinanceTickerDto::getSymbol, t -> t));
  }
}

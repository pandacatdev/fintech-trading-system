package com.aquariux.fintech.trading.client;

import com.aquariux.fintech.trading.client.dto.HuobiResponseDto;
import com.aquariux.fintech.trading.client.dto.HuobiResponseDto.HuobiTickerDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class HuobiPriceClient {
  private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";

  private final RestClient restClient;

  public Map<String, HuobiTickerDto> fetchPrices(List<String> symbols) {
    var huobiResponse = restClient.get()
        .uri(HUOBI_URL)
        .retrieve()
        .body(HuobiResponseDto.class);

    // TODO: Handle errors and nulls appropriately

    return huobiResponse.getData().stream()
        .filter(t -> symbols.contains(t.getSymbol().toUpperCase()))
        .collect(Collectors.toMap(t -> t.getSymbol().toUpperCase(), t -> t));
  }
}

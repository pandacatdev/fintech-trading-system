package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.client.HuobiClient;
import com.aquariux.fintech.trading.client.dto.HuobiResponseDto.HuobiTickerDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class HuobiPriceService {
  private final HuobiClient huobiClient;
  private static final Logger log = LoggerFactory.getLogger(HuobiPriceService.class);

  public Map<String, HuobiTickerDto> fetchPrices(List<String> symbols) {
    log.info("Fetching Huobi prices for symbols: {}", symbols);
    var huobiResponse = huobiClient.getTickers();
    log.debug("Received {} tickers from Huobi", huobiResponse.getData().size());
    return huobiResponse.getData().stream()
        .filter(t -> symbols.contains(t.getSymbol().toUpperCase()))
        .collect(Collectors.toMap(t -> t.getSymbol().toUpperCase(), t -> t));
  }
}

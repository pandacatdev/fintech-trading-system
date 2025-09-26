package com.aquariux.fintech.trading.client;

import com.aquariux.fintech.trading.client.dto.BinanceTickerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "binanceClient", url = "https://api.binance.com")
public interface BinanceClient {
    @GetMapping("/api/v3/ticker/bookTicker")
    BinanceTickerDto[] getBookTickers();
}


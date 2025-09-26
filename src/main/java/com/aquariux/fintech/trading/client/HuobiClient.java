package com.aquariux.fintech.trading.client;

import com.aquariux.fintech.trading.client.dto.HuobiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "huobiClient", url = "https://api.huobi.pro")
public interface HuobiClient {
    @GetMapping("/market/tickers")
    HuobiResponseDto getTickers();
}


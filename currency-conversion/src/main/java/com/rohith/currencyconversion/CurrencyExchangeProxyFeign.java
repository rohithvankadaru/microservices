package com.rohith.currencyconversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "currency-exchange", url = "localhost:8000")
@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxyFeign {

    @GetMapping("/currency-exchange")
    public CurrencyConversion retrieveExchangeValue(@RequestParam("from") String from,
                                                       @RequestParam("to") String to);
}

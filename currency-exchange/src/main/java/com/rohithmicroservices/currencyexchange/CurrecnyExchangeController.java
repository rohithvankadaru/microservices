package com.rohithmicroservices.currencyexchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrecnyExchangeController {

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange")
    public CurrencyExchange retrieveExchangeValueParam(@RequestParam("from") String from,
                                                  @RequestParam("to") String to){
        String port = environment.getProperty("local.server.port");
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from,to);
        currencyExchange.setEnvironment(port);
        System.out.print(5);

        return currencyExchange;
    }


    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,
                                                 @PathVariable String to){

        String port = environment.getProperty("local.server.port");
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from,to);
        currencyExchange.setEnvironment(port);
        System.out.print(5);

        return currencyExchange;
    }
}
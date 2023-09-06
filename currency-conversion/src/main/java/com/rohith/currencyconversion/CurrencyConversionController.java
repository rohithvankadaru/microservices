package com.rohith.currencyconversion;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;


@Slf4j
@RestController
public class CurrencyConversionController {
    static int count = 0;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final RestTemplate restTemplate;

    private final CurrencyExchangeProxyFeign currencyExchangeProxyFeign;

    @Autowired
    public CurrencyConversionController(CurrencyExchangeProxyFeign currencyExchangeProxyFeign, RestTemplate restTemplate){
        this.currencyExchangeProxyFeign = currencyExchangeProxyFeign;
        this.restTemplate= restTemplate;
    }


    @GetMapping("/currency-conversion-web-client/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackMethod")
    @ResponseStatus(HttpStatus.CREATED)
//    @Retry(name = "default", fallbackMethod = "fallBackMethod")
    public ResponseEntity calculateCurrencyConversionWebClient(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity){
        log.info("ENTERED TO WEB-CLIENT ZONE");
        CurrencyConversion currencyConversion = webClientBuilder.build().get()
                            .uri("http://currency-exchange/currency-exchange",
                                    uriBuilder -> uriBuilder
                                            .queryParam("from",from)
                                            .queryParam("to",to)
                                            .build())
                            .retrieve()
                            .bodyToMono(CurrencyConversion.class)
                            .block();
        log.info("call done " + currencyConversion.getEnvironment());

        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" "+"WebClient");
        return new ResponseEntity<>(currencyConversion, HttpStatus.CREATED);
    }

    private ResponseEntity fallBackMethod(Exception e){
        log.info(e.getMessage());
        return new ResponseEntity<>("SERVER DOWN",HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackMethod")
    public ResponseEntity calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity){
        log.info("ENTERED TO REST-TEMPLATE ZONE");
        HashMap<String, String > uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to",to);
        ResponseEntity<CurrencyConversion> entity = restTemplate.getForEntity("http://currency-exchange/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
        CurrencyConversion currencyConversion = entity.getBody();
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" "+"RestTemplate");

        return new ResponseEntity<>(currencyConversion, HttpStatus.CREATED);
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackMethod")
    public ResponseEntity calculateCurrencyConversionFeign(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity){
        CurrencyConversion currencyConversion = currencyExchangeProxyFeign.retrieveExchangeValue(from, to);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" Feign");

        return new ResponseEntity<>(currencyConversion,HttpStatus.CREATED);
    }


    @GetMapping("/currency-conversion-web-client-Dup/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackMethod")
    @ResponseStatus(HttpStatus.CREATED)
//    @Retry(name = "default", fallbackMethod = "fallBackMethod")
    public ResponseEntity calculateCurrencyConversionWebClientDuplicate(@PathVariable String from,
                                                               @PathVariable String to,
                                                               @PathVariable BigDecimal quantity){
        log.info("ENTERED TO WEB-CLIENT ZONE DUP");
        HashMap<String,String> uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to", to);
        CurrencyConversion currencyConversion = webClientBuilder.build()
                        .get()
                        .uri("http://currency-exchange/currency-exchange/from/{from}/to/{to}",uriVariables)
                        .retrieve()
                        .bodyToMono(CurrencyConversion.class)
                        .block();
        log.info("call done " + currencyConversion.getEnvironment());

        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" "+"WebClientDup");
        return new ResponseEntity<>(currencyConversion, HttpStatus.CREATED);
    }

}
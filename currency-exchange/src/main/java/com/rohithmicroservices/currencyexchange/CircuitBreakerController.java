package com.rohithmicroservices.currencyexchange;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class CircuitBreakerController {

    @Autowired
    Environment environment;

    @GetMapping("/sampleApi")
//    @Retry(name = "sampleApi", fallbackMethod = "hardcodedResponse")
    @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
    @RateLimiter(name = "sample")
    @Bulkhead(name = "sample")
    public String sampleApi(){

        log.info("REQUEST RECEIVED");
        String s = new RestTemplate().getForEntity("http://localhost:8080/get",String.class).getBody();
        return "hsdwiud";
    }

    private String hardcodedResponse(Exception e){
        return "<-------------------------------SERVER DOWN------------------------------->";
    }
}

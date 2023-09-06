package com.rohith.microservices.limitsservice.controller;

import com.rohith.microservices.limitsservice.Configuration.Configuration;
import com.rohith.microservices.limitsservice.Entity.Limits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

    @Autowired
    Configuration configuration;

    @GetMapping("/limits")
    public Limits getLimits(){
        return Limits.builder()
                .maximum(configuration.getMaximum())
                .minimum(configuration.getMinimum())
                .build();
    }
}

package com.rohithmicroservices.currencyexchange;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CurrencyExchange {

       @Id
       Long id;

       @Column(name = "currency_from")
       String from;

       @Column(name = "currency_to")
       String to;

       BigDecimal conversionMultiple;
       String environment;
}
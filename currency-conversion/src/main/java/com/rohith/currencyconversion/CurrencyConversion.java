package com.rohith.currencyconversion;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyConversion {

    Long id;
    String from;
    String to;
    BigDecimal quantity;
    BigDecimal conversionMultiple;
    BigDecimal totalCalculatedAmount;
    String environment;
}

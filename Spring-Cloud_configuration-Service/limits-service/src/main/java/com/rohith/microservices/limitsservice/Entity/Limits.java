package com.rohith.microservices.limitsservice.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Limits {

    int minimum;
    int maximum;
}

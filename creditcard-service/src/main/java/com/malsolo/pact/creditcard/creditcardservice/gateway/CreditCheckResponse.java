package com.malsolo.pact.creditcard.creditcardservice.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCheckResponse {
    private Score score;
    private String uuid;
}

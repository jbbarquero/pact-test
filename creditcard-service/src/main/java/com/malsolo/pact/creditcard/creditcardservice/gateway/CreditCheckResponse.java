package com.malsolo.pact.creditcard.creditcardservice.gateway;

import lombok.Data;

@Data
public class CreditCheckResponse {
    private final Score score;
    private final String uuid;
}

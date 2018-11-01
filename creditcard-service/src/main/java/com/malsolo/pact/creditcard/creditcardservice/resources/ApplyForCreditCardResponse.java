package com.malsolo.pact.creditcard.creditcardservice.resources;

import lombok.Data;

@Data
public class ApplyForCreditCardResponse {
    private final Status status;
    private final String uuid;
}

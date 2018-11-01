package com.malsolo.pact.creditcard.creditcardservice.resources;

import lombok.Data;

@Data
public class ApplyForCreditCardRequest {
    private final int citizenNumber;
    private final CardType cardType;

}

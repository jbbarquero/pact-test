package com.malsolo.pact.creditcard.creditcardservice.gateway;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;


@Getter
public class CreditCheckRequest {
    private final int citizenNumber;
    private final String requestedDate = LocalDate.now().toString();
    private final String uuid = UUID.randomUUID().toString();

    public CreditCheckRequest(int citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

}

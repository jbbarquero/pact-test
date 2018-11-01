package com.malsolo.pact.creditcard.creditcardservice.service;

import com.malsolo.pact.creditcard.creditcardservice.gateway.CreditCheckGateway;
import com.malsolo.pact.creditcard.creditcardservice.gateway.CreditCheckResponse;
import com.malsolo.pact.creditcard.creditcardservice.gateway.Score;
import com.malsolo.pact.creditcard.creditcardservice.resources.ApplyForCreditCardRequest;
import com.malsolo.pact.creditcard.creditcardservice.resources.ApplyForCreditCardResponse;
import org.springframework.stereotype.Service;

import static com.malsolo.pact.creditcard.creditcardservice.gateway.Score.HIGH;
import static com.malsolo.pact.creditcard.creditcardservice.gateway.Score.LOW;
import static com.malsolo.pact.creditcard.creditcardservice.resources.CardType.GOLD;
import static com.malsolo.pact.creditcard.creditcardservice.resources.Status.DENIED;
import static com.malsolo.pact.creditcard.creditcardservice.resources.Status.GRANTED;

@Service
public class CreditCheckService {

    private final CreditCheckGateway creditCheckGateway;

    public CreditCheckService(CreditCheckGateway creditCheckGateway) {
        this.creditCheckGateway = creditCheckGateway;
    }

    public ApplyForCreditCardResponse doCheckForCitizen(ApplyForCreditCardRequest applyForCreditCardRequest) {

        final CreditCheckResponse creditCheckResponse = creditCheckGateway.doCreditCheckForCitizen(applyForCreditCardRequest.getCitizenNumber());

        final Score score = creditCheckResponse.getScore();
        final String uuid = creditCheckResponse.getUuid();

        if (applyForCreditCardRequest.getCardType() == GOLD) {
            if (score == HIGH) {
                return new ApplyForCreditCardResponse(GRANTED, uuid);
            } else if(score == LOW) {
                return new ApplyForCreditCardResponse(DENIED, uuid);
            }
        }

        throw new RuntimeException("Card and score not yet implemented");
    }
}

package com.malsolo.pact.creditcard.creditcardservice.resources;

import com.malsolo.pact.creditcard.creditcardservice.service.CreditCheckService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditcardController {

    private final CreditCheckService creditCheckService;

    public CreditcardController(CreditCheckService creditCheckService) {
        this.creditCheckService = creditCheckService;
    }

    @PostMapping(value = "/credit-card-applications", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApplyForCreditCardResponse applyForCreditCard(@RequestBody final ApplyForCreditCardRequest applyForCreditCardRequest) {
        return creditCheckService.doCheckForCitizen(applyForCreditCardRequest);
    }


}

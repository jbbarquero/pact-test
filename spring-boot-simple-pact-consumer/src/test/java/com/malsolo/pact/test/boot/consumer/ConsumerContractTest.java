package com.malsolo.pact.test.boot.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

public class ConsumerContractTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("pact_producer", "localhost", 9191, this);

    @Pact(provider="test_provider", consumer="test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return null; //TODO
    }

    @Test
    @PactVerification("test_provider")
    public void runTest() {

    }
}

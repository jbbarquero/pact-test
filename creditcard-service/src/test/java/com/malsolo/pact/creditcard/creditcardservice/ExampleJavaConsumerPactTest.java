package com.malsolo.pact.creditcard.creditcardservice;

import au.com.dius.pact.consumer.ConsumerPactTestMk2;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.ConsumerPactTest;
import au.com.dius.pact.model.PactFragment;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ExampleJavaConsumerPactTest extends ConsumerPactTestMk2 {

    @Override
    protected RequestResponsePact createPact(PactDslWithProvider pactDslWithProvider) {
        Map<String, String> headers = new HashMap<>();
        headers.put("testreqheader", "testreqheadervalue");

        return pactDslWithProvider
                .given("test state") // NOTE: Using provider states are optional, you can leave it out
                .uponReceiving("ExampleJavaConsumerPactTest test interaction")
                .path("/")
                .method("GET")
                .headers(headers)
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\"responsetest\": true, \"name\": \"harry\"}")
                .given("test state 2") // NOTE: Using provider states are optional, you can leave it out
                .uponReceiving("ExampleJavaConsumerPactTest second test interaction")
                .method("OPTIONS")
                .headers(headers)
                .path("/second")
                .body("")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body("")
                .toPact();
    }



    @Override
    protected String providerName() {
        return "test_provider";
    }

    @Override
    protected String consumerName() {
        return "test_consumer";
    }

    @Override
    protected void runTest(MockServer mockServer) throws IOException {
        Assert.assertEquals(new ConsumerClient(mockServer.getUrl()).options("/second"), 200);
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("responsetest", true);
        expectedResponse.put("name", "harry");
        assertEquals(new ConsumerClient(mockServer.getUrl()).getAsMap("/", ""), expectedResponse);
        assertEquals(new ConsumerClient(mockServer.getUrl()).options("/second"), 200);
    }
}
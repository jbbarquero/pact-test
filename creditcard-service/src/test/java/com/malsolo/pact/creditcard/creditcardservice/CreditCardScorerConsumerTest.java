package com.malsolo.pact.creditcard.creditcardservice;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.malsolo.pact.creditcard.creditcardservice.gateway.CreditCheckRequest;
import com.malsolo.pact.creditcard.creditcardservice.gateway.CreditCheckResponse;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardScorerConsumerTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("test_provider", "localhost", 8080, this);

    @Pact(provider = "creditcard-scorer", consumer = "creditcard-service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .integerType("citizenNumber", 1234)
                .date("requestedDate")
                .uuid("uuid")
                .asBody();

        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("score", "HIGH")
                .uuid("uuid")
                .asBody();

        return builder
                .given("There is a GOLD card request for the citizen 1234")
                .uponReceiving("A score request for a GOLD card for the citizen 1234")
                .path("/credit-scores")
                .method("POST")
                .body(requestBody)
                .willRespondWith()
                .status(200)
                .body(responseBody)
                .toPact();
    }

    @Test
    @PactVerification()
    @Ignore
    public void shouldGrantApplicationWhenCreditScoreIsHigh() {
        String url = "http://localhost:" + mockProvider.getPort();

        System.out.println(url);

        WebTestClient webClient = WebTestClient.bindToServer().baseUrl(url).build();

        CreditCheckRequest request = new CreditCheckRequest(1234);

        webClient.post().uri("/credit-scores")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreditCheckResponse.class)
                .consumeWith(response -> {
                            assertThat(response).isNotNull();
                            assertThat(response.getResponseBody()).isNotNull();
                            assertThat(response.getResponseBody().getScore()).isEqualTo("HIGH");
                            //assertThat(response.getResponseBody().getUuid()).isEqualTo("UUID");
                        }
                );
    }

}
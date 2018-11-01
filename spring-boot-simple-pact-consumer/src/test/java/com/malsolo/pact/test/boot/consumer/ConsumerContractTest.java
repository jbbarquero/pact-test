package com.malsolo.pact.test.boot.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsumerContractTest {

    //@Autowired
    private ConsumerClient consumerClient = new ConsumerClient(new RestTemplate());
    //@Value("${producer.url}")
    private String producerUrl = "http://localhost:9191/producer";

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("boot_simple_pact_producer", "localhost", 9191, this);

    @Pact(consumer="boot_simple_pact_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("There is a producer that expects requests")
            .uponReceiving("A POST request to the producer")
                .path("/producer")
                .method("POST")
            .willRespondWith()
                .status(200)
                .headers(
                        new HashMap<String, String>() {
                            {
                                put("Content-Type", "application/json");
                            }
                        }
                )
                .body("{\"text\":\"Text\",\"date\":\"2018-11-01T12:49:55.993\", \"count\":1000}")
                .toPact();    }

    @Test
    @PactVerification("boot_simple_pact_producer")
    public void runTest() {
        Produced produced = consumerClient.consumerProducer(producerUrl,
                LocalDateTime.now(ZoneOffset.UTC));
        assertThat(produced).isNotNull();
        assertThat(produced.getText()).isEqualTo("Text");
        assertThat(produced.getCount()).isEqualTo(1000);
        assertThat(produced.getDate()).isBefore(Date.from(
                LocalDateTime.of(2018, 11, 1, 12, 50, 0)
                .toInstant(ZoneOffset.UTC)));
    }
}

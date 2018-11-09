package com.malsolo.pact.test.boot.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsumerContractTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ConsumerClient consumerClient;// = new ConsumerClient(new RestTemplate());
    @Autowired
    private RestTemplate restTemplate;

    private static int producerRandomPort;

    @BeforeClass
    public static void assignProducerRandomPort() {
        producerRandomPort = SocketUtils.findAvailableTcpPort();
    }

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("boot_simple_pact_producer", "localhost", producerRandomPort, this);

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
                .body("{\"text\":\"Text\",\"date\":\"2018-11-01T12:49:55.993+0000\", \"count\":1000}")
            .toPact();    }

    @Test
    @PactVerification("boot_simple_pact_producer")
    public void shouldResponseTheProducerApplication() {

        //To verify that Spring boot test is working

        String consumerUrl = String.format("http://localhost:%d/actuator/info", port);
        System.out.printf("***** Verify that Spring Boot test is working with local server at %s\n", consumerUrl);
        String string = restTemplate.getForObject(consumerUrl, String.class);
        assertThat(string).isNotNull();
        assertThat(string).isEqualTo("{}");

        String producerUrl = String.format("http://localhost:%d/producer", producerRandomPort);
        System.out.printf("***** CONTRACT TESTING WITH PRODUCER: %s\n", producerUrl);

        //Actual contract test
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

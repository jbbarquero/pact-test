package com.malsolo.pact.test.boot.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class ConsumerClient {

    private final RestTemplate restTemplate;

    public ConsumerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Produced consumerProducer(String url, LocalDateTime dateTime) {
        ResponseEntity<Produced> response = restTemplate.postForEntity(url, dateTime, Produced.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new NotFoundException();
        }
        return response.getBody();
    }
}

package com.malsolo.pact.test.boot.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
public class ConsumerController {

    private final RestTemplate restTemplate;
    private final String urlProducer;

    public ConsumerController(RestTemplate restTemplate, @Value("${producer.url}") String producerUrl) {
        this.restTemplate = restTemplate;
        urlProducer = producerUrl;
    }

    @GetMapping("/consumer")
    public ResponseEntity<Consumed> consumerJson(@RequestParam(required = false) String validDate) {
        LocalDateTime validTime;
        try {
            if (validDate == null) {
                validDate = LocalDateTime.now().toString();
            }
            validTime = LocalDateTime.parse(validDate);
        } catch (Exception e) {
            throw new BadRequestException();
        }

        ResponseEntity<Produced> producedResponse = restTemplate.postForEntity(urlProducer, validTime, Produced.class);

        if (producedResponse.getStatusCode() != HttpStatus.OK) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok(
          new Consumed(
                  producedResponse.getBody().getText(),
                  producedResponse.getBody().getDate(),
                  producedResponse.getBody().getCount(),
                  new Random().nextBoolean()
          )
        );
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Produced not found")
    public class NotFoundException extends RuntimeException {}

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Bad request")
    public class BadRequestException extends RuntimeException {}
}


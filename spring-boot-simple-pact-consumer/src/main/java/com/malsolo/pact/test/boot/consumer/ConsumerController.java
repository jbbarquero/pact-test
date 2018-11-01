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

    private final ConsumerClient consumerClient;
    private final String urlProducer;

    public ConsumerController(ConsumerClient consumerClient, @Value("${producer.url}") String producerUrl) {
        this.consumerClient = consumerClient;
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

            Produced produced = consumerClient.consumerProducer(urlProducer, validTime);

        return ResponseEntity.ok(
          new Consumed(
                  produced.getText(),
                  produced.getDate(),
                  produced.getCount(),
                  new Random().nextBoolean()
          )
        );
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Bad request")
    public class BadRequestException extends RuntimeException {}
}


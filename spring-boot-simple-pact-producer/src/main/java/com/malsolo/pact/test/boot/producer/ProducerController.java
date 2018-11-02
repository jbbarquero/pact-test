package com.malsolo.pact.test.boot.producer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@RestController
public class ProducerController {

    /*
    * $ curl -X POST localhost:9191/producer -d "2018-11-03T00:31:00.578" -H "Content-type:application/JSON"
    */
    @PostMapping("/producer")
    public ResponseEntity<Produced> producerJSON(/*@RequestBody(required = false) LocalDateTime dateTime*/) {
        LocalDateTime dateTime = null;
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        byte[] bytes = new byte[10];
        new Random().nextBytes(bytes);
        return ResponseEntity.ok(new Produced(
                String.format("Tet: %s", new String(bytes, Charset.defaultCharset())),
                Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()),
                new Random().nextInt()
                )
        );
    }
}

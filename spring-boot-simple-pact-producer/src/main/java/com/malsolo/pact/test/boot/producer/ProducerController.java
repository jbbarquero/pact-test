package com.malsolo.pact.test.boot.producer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class ProducerController {

    /*
    * $ curl -X POST localhost:9191/producer -d "2018-11-03T00:31:00.578" -H "Content-type:application/JSON"
    */
    @PostMapping("/producer")
    public ResponseEntity<Produced> producerJSON(/*@RequestBody(required = false) LocalDateTime dateTime*/) {
        LocalDateTime dateTime = null;
        if (dateTime == null) {
            dateTime = LocalDateTime.parse("2018-11-01T12:49:55.993");
        }
        Produced produced = new Produced(
                "Text",
                Date.from(dateTime.atZone(ZoneId.of("UTC")).toInstant()),
                1000
        );
        return ResponseEntity.ok(produced);
    }
}

package com.malsolo.pact.test.boot.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProducerContractTest {

    @LocalServerPort
    private int port;

    @Test
    public void testMarkerMethod() {
        String url = String.format("http://localhost:%d/actuator/info", port);
        String response = new RestTemplate().getForObject(url, String.class);
        System.out.printf("***** Response from %s [%s]\n",response, url);
    }
}

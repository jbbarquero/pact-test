package com.malsolo.pact.test.boot.producer;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@RunWith(PactRunner.class) // Say JUnit to run tests with custom Runner
@Provider("boot_simple_pact_producer") // Set up name of tested provider
//@PactFolder("pacts") // Point where to find pacts (See also section Pacts source in documentation)
@PactBroker(host = "localhost", port = "9000", protocol = "http")
public class ProducerContractTest {

    // NOTE: this is just an example of embedded service that listens to requests, you should start here real service
    //Rule will be applied once: before/after whole contract test suite
    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(options().port(8888));//.httpsPort(8889));

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    private static int randomPort;

    @BeforeClass //Method will be run once: before whole contract test suite
    public static void setUpService() {
        System.out.println("Run DB, create schema. Run service...");
    }

    @BeforeClass
    public static void RunSpringApplication() {
        randomPort = SocketUtils.findAvailableTcpPort();
        System.getProperties().put( "server.port", randomPort);
        SpringApplication.run(SpringBootSimplePactProducerApplication.class);
        System.out.printf("##### APPLICATION RUNNING ON PORT %d\n", randomPort);

        System.getProperties().put("pact.verifier.publishResults", "true");
    }

    @Before //Method will be run before each test of interaction
    public void before() {
        // Rest data
        // Mock dependent service responses
        // ...
        stubFor(get(urlEqualTo("/third/party/service"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"hello\": \"world\"}")));
    }

    @State({"default", "no-data"}) // Method will be run before testing interactions that require "default" or "no-data" state
    public void toDefaultState() {
        // Prepare service before interaction that require "default" state
        // ...
        System.out.println("Now service in default state");
    }

    @State("with-data") // Method will be run before testing interactions that require "with-data" state
    public void toStateWithData(Map data) {
        // Prepare service before interaction that require "with-data" state. The provider state data will be passed
        // in the data parameter
        // ...
        if (data == null) {
            System.out.println("Data null");
            data = new HashMap();
        }
        System.out.println("Now service in state using data " + data);
    }

    @State("There is a producer that expects requests")
    public void someProviderState(Map<String, Object> providerStateParameters) {
        System.out.println("There is a producer that expects requests TEST");
        // Do what you need to set the correct state
    }

    @TestTarget // Annotation denotes Target that will be used for tests
    public final Target target = new HttpTarget(randomPort); // Out-of-the-box implementation of Target (for more information take a look at Test Target section)

    @Test
    public void testMarkerMethod() {
        int port = 0;
        String url = String.format("http://localhost:%d/actuator/info", port);
        String response = new RestTemplate().getForObject(url, String.class);
        System.out.printf("***** Response from %s [%s]\n",response, url);
    }
}

package ch.bluesky.spring.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GreetingControllerIT {

  @LocalServerPort
  private int port;

  private URL base;

  @Autowired
  private TestRestTemplate restTemplate;

  @Before
  public void setUp() throws Exception {
    this.base = new URL("http://localhost:" + port);
  }

  @Test
  public void shouldRespondHelloWorld() {
    final ResponseEntity<String> response = restTemplate
        .getForEntity(base.toString() + "/greeting", String.class);

    assertThat(response.getBody(), equalTo("Hello World!"));
  }
}

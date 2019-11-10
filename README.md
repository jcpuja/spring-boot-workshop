# Spring Boot Workshop

Ein Workshop, um Spring Boot zu entdecken. Für Entwickler, die keine oder wenig Erfahrung mit dem Spring-Ökosystem haben.

Der Workshop hat die Ziele, folgende Fragen zu beantworten:

- Wie ist Spring Boot grob strukturiert (starters, maven plugin)?
- Wie kann ich eine Rest API mit Spring Boot bauen?

Einen von:
- Wie kann ich Spring Boot Anwendungen testen?
- Was ist Spring Data und wie integriert es sich mit meiner Anwendung?

Extra:
- Wie kann ich meine Anwendung sichern mit Spring Security?

# Voraussetzungen

- JDK 8+
- Maven 3.2+

# Schritte

## 1. Hello world

Das Ziel hier ist, eine Hello World REST API zu bauen. Wir möchten, dass unsere API über HTTP auf `GET /greeting` mit `Hello World!` antwortet.

#### 1.1 Maven-Projekt aufsetzen:

- Parent POM deklarieren: `spring-boot-starter-parent`
- Dependency zum Web Starter deklarieren: `spring-boot-starter-web`
- Das Spring Boot Maven Plugin ins Build einbinden: `spring-boot-maven-plugin`

Die dependencies brauchen hier keine Versionsdeklaration; sie werden vom Parent Pom managed (transitiv, von `org.springframework.boot:spring-boot-dependencies`).

- Die Application-Klasse erstellen:
  - Mit `@SpringBootApplication` annotiert
  - Hat nur eine Java Main Methode, die Spring bootstrapt: `SpringApplication.run(Application.class, args);` 
- Server starten: `mvn spring-boot:run`. Cooles ASCII-Art erwarten 😎.

#### 1.2 Controller hinzufügen

Bis jetzt läuft auf dem Server nichts. Wir müssen einen REST-Endpoint bauen.

REST-Endpoints werden in sogenannten `Controller`s definiert. Vermutlich kommt das aus dem MVC-Pattern, auf dem Spring Web-MVC aufgebaut wurde. Sie sind die Spring-Äquivalenten zu Java EE `Resource`-Klassen.

- Controller-Klasse hinzufügen (evtl im eigenen Package): `GreetingController`
- Mit `@RestController` annotieren
- Eine Methode hinzufügen, die die gewünschte Begrüssung als String zurückgibt
- Methode mit `@RequestMapping` annotieren, so dass HTTP `GET` requests auf dem Pfad `/greeting` zu dieser Methode dispatched werden.

Nach einem Neustart des Servers sollte unsere Hello World REST API funktionieren.

#### 1.3 (Bonus) Production-Ready Hello World

Mit einem zusätzlichen Starter können wir unserer REST API Observability geben: Actuator.

- Actuator dependency hinzufügen: `spring-boot-starter-actuator`

Mit einem `GET /actuator` erhalten wir eine Liste von Metriken und Management endpoints. Mit z.B. `GET /actuator/health` können wir prüfen, ob der Server erforlgreich hochgefahren ist.

## 2. Testing

*Testing ist wichtig 😉*

[Testpyramide](https://martinfowler.com/bliki/TestPyramid.html): Die zahlreichsten Tests sollten schnelle, feingeschnittene Tests sein. Diese kann man mit normalem JUnit und ggf. Mockito schreiben (keine besondere Spring-Abhängigkeiten).

Für Szenarien, wo wir mehrere Schichten testen wollen, oder sogar die ganze App, kann uns **Spring Test** helfen. Wir können beliebig viele Beans in einen Test Kontext mitnehmen und zusammenspielen lassen.

#### 2.1 Testklasse anlegen

- Abhängigkeit auf Spring Test Starter deklarieren: `spring-boot-starter-test`
- Testklasse für `GreetingController` anlegen
- `SpringRunner` verwenden
- Spring Boot Test Autokonfiguration aktivieren: `@SpringBootTest`
- Leeren Test hinzufügen, um unseren Test-Kontext zu prüfen
- Test ausführen, cooles ASCII-Art erwarten 😎 

Als Vorbereitung zum Test wird von Spring der Test-Kontext aufgebaut, als Default mit allen Klassen der Anwendung. Mit Parameter der `@SpringBootTest`-Annotation können wir konfigurieren, wie genau der Test-Kontext aufgebaut werden muss.

#### 2.2 REST API test schreiben

MockMVC von Spring Test erlaubt uns, einfaches Testing von HTTP APIs zu schreiben.

- MockMVC Autokonfiguration aktivieren: `@AutoConfigureMockMvc`
- Ein Bean des Typs `MockMvc` in der Testklasse einbinden
- Ein Test schreiben, der folgendes prüft:
  - Wenn ich ein Request auf `GET /greeting` verschicke
  - Dann erwarte ein HTTP Status von `200 OK`
  - Und erwarte ein Response Body mit dem Text `Hello World!`
 
**Hinweise**:

- Das `MockMvc`-Bean bietet eine Fluent API, um diese erwarteten Request-Response Dialog abzubilden. Wir können bei der Methode `mockMvc.perform()` anfangen.
- Instanzen von `RequestBuilder`s um den Aufruf aufzubauen kann man über die Utility-Klasse `MockMvcRequestBuilders` erhalten.
- URL: `/greeting` (für MockMvc kein Hostname nötig)
- Instanzen von `RequestMatcher`s für Expectations kann man über die Utility-Klasse `MockMvcResultMatchers` erhalten.

#### 2.3 (Bonus) Integrationstest schreiben

Ziel: SpringBootTest `webEnvironment` verwenden, um einen echten Server zu starten, und Integrationstests dagegen ausführen.

<details>
    <summary>Antwort:</summary>
   
```java
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

```
</details>

# Spring Boot Workshop

Ein Workshop, um Spring Boot zu entdecken. Für Entwickler, die keine oder wenig Erfahrung mit dem Spring-Ökosystem haben.

Der Workshop hat die Ziele, folgende Fragen zu beantworten:

- Wie ist Spring Boot grob strukturiert (parent pom, starters)?
- Wie kann ich eine Rest API mit Spring Boot bauen?
- Wie kann ich Spring Boot Anwendungen testen?

Bonus falls Zeit übrig:
- Wie kann ich Datenhaltung mit Spring Data vereinfachen?

## Voraussetzungen

- JDK 8 oder höher
- Maven 3.2 oder höher
- Dein lieblings-Java IDE

## Anleitung

Dieses Repository enthält auf `master` ein leeres Maven Projekt. Das ist der Startpunkt unseres Workshops, bitte clonen! 

In jedem Abschnitt unter **Aufgaben** sind Ziele definiert. Diese können mit den Instruktionen in den nachfolgenden Bullet Point Listen erreicht werden.

Fertige Lösungen sind in den branches zu finden, jeweils 1 branch pro Abschnitt.

Einige Abschnitte sind als "Bonus" markiert. Diese sind da, falls Zeit übrig ist oder ihr euch langweilt!

## Aufgaben

### 1. Hello world

Wir möchten eine klassiche Hello World REST API bauen. Zuerst soll unsere API über HTTP auf `GET /greeting` mit `Hello World!` antworten.

#### 1.1 Maven-Projekt aufsetzen:

**Ziel:** Spring Boot in unseren Maven Projekt einbinden, ein leerer Server starten. 

- Parent POM deklarieren: `spring-boot-starter-parent`
- Dependency zum Web Starter deklarieren: `spring-boot-starter-web`
- Das Spring Boot Maven Plugin ins Build einbinden: `spring-boot-maven-plugin`

Die dependencies brauchen hier keine Versionsdeklaration; sie werden vom Parent Pom managed (transitiv, von `org.springframework.boot:spring-boot-dependencies`).

- Die Application-Klasse erstellen:
  - Mit `@SpringBootApplication` annotiert
  - Hat nur eine Java Main Methode, die Spring bootstrapt: `SpringApplication.run(Application.class, args);` 
- Server starten: `mvn spring-boot:run`. Cooles ASCII-Art erwarten 😎

#### 1.2 Controller hinzufügen

Bis jetzt läuft auf dem Server nichts. Wir müssen einen REST-Endpoint bauen.

**Ziel:** Ein REST-Endpoint zu unserem Server hinzufügen, der auf `GET /greeting` mit `Hello World!` antwortet.

REST-Endpoints werden in sogenannten `Controller`s definiert. Vermutlich kommt das aus dem MVC-Pattern, auf dem Spring Web-MVC aufgebaut wurde. Sie sind die Spring-Äquivalenten zu Java EE `Resource`-Klassen.

- Controller-Klasse hinzufügen (evtl im eigenen Package): `GreetingController`
- Mit `@RestController` annotieren
- Eine Methode hinzufügen, die die gewünschte Begrüssung als String zurückgibt
- Methode mit `@RequestMapping` annotieren, so dass HTTP `GET` requests auf dem Pfad `/greeting` zu dieser Methode dispatched werden.

Nach einem Neustart des Servers sollte unsere Hello World REST API funktionieren.

#### 1.3 Service hinzufügen

Im Sinn vom Single Responsibility Principle sollte nicht viel mehr als HTTP Request-Logik im Controller passieren. Wir könnten die Ermittlung des Greetings in ein Service extrahieren.

**Ziel:** Ein Service erstellen, der die Begrüssung liefert, und dieser aus dem Controller aufrufen.

- Neue Klasse anlegen, evtl. im eigenen Package: `GreetingService`
- Annotation hinzufügen: `@Service`
- Methode hinzufügen, die "Hello World!" zurückgibt
- Ein Feld vom Typ `GreetingService` im `GreetingController` definieren und im Constructor initialisieren
- Die Service-Methode aufrufen, um die Begrüssung zu ermitteln

**Erklärung:**

Klassen, die mit `@Component` annotiert sind, werden automatisch von Spring Boot erkannt und für Injection bereitgestellt (`@Service` ist eine Spezialisierung von `@Component`).

Alle Kandidaten für Injection werden beim Bootstrappen instanziert und in einen **Application Context** gesammelt. Solche Objekte, die von Spring managed werden, bezeichnet man als **Beans**.

Spring hat automatisch das `GreetingService` Bean aus dem Application Context zum Konstruktor von `GreetingController` geliefert. Um diese Injection explizit zu machen, könnten wir die `@Autowired` Annotation verwenden (äquivalent zu `@Inject` in Java EE). `@Autowired` ist auch benötigt bei Field oder Setter injection. 

Wir können den Application Context in unserem Code einbinden, z.B. für Debugging.

<details>
    <summary>Application Context Injection Beispiel</summary>
    
```java
import org.springframework.context.ApplicationContext;

// Feld wird im Konstruktor injected
private final ApplicationContext context;

// In einer Methode: die Namen aller Beans ausgeben, die sich im Kontext befinden
Arrays.stream(context.getBeanDefinitionNames())
        .sorted()
        .forEach(System.out::println);
```
</details>


#### 1.4 (Bonus) Production-Ready Hello World

**Ziel:** Spring Boot actuator Endpoints aktivieren.

Mit einem zusätzlichen Starter können wir unserer REST API Observability geben: Actuator.

- Actuator dependency hinzufügen: `spring-boot-starter-actuator`

Mit einem `GET /actuator` erhalten wir eine Liste von Metriken und Management endpoints. Mit z.B. `GET /actuator/health` können wir prüfen, ob der Server erforlgreich hochgefahren ist.

### 2. Testing

_**Testing ist wichtig 😉**_

[Testpyramide](https://martinfowler.com/bliki/TestPyramid.html): Die zahlreichsten Tests sollten schnelle, feingeschnittene Tests sein. Diese kann man mit normalem JUnit und ggf. Mockito schreiben (keine besondere Spring-Abhängigkeiten).

Für Szenarien, wo wir mehrere Schichten testen wollen, oder sogar die ganze App, kann uns **Spring Test** helfen. Wir können beliebig viele Beans in einen Test Application Context mitnehmen und zusammenspielen lassen.

#### 2.1 Testklasse anlegen

**Ziel:** Eine Testklasse aufbereiten, die einen Spring Boot Test Context startet.

- Abhängigkeit auf Spring Test Starter deklarieren: `spring-boot-starter-test`
- Testklasse für `GreetingController` anlegen
- `SpringRunner` verwenden
- Spring Boot Test Autokonfiguration aktivieren: `@SpringBootTest`
- Leeren Test hinzufügen, um unseren Test-Kontext zu prüfen
- Test ausführen, cooles ASCII-Art erwarten 😎 

Als Vorbereitung zum Test wird von Spring der Test Application Context aufgebaut, als Default mit allen Klassen der Anwendung. Mit Parameter der `@SpringBootTest`-Annotation können wir konfigurieren, wie genau der Test Application Context aufgebaut werden muss.

#### 2.2 REST API test schreiben

**Ziel:** Ein Testcase schreiben, der mit Mock MVC ein Aufruf zu unserer API simuliert und die Antwort auf Erwartungen (Status code, Inhalt) überprüft.

Mock MVC von Spring Test erlaubt uns, einfaches Testing von HTTP APIs zu schreiben.

- Mock MVC Autokonfiguration aktivieren: `@AutoConfigureMockMvc`
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

**Ziel:** SpringBootTest `webEnvironment` verwenden, um einen echten Server zu starten, und Integrationstests dagegen ausführen.

- `@SpringBootTest` konfigurieren: `webEnvironment` auf `RANDOM_PORT` setzen
- Port als Feld der Testklasse definieren, mit `@LocalServerPort` wird dieser befüllt
- Mit `TestRestTemplate` kann mit dem Test-Server kommuniziert werden
- Test schreiben, der ein Aufruf auf `http://localhost:<port>/greeting` ausführt, und "Hello World!" als Response erwartet.

### 3 (Bonus) Spring Data

Spring Data abstrahiert die Persistenz-Schicht. Ein zentrales Konzept dabei ist das Repository Pattern aus Domain-Driven Design. Spring Data bietet Repositories mit fertigen CRUD-Funktionalitäten für viele Persistenz-Technologien. Diese lassen sich sehr einfach für eigene Logik erweitern.

Wir werden unsere Begrüssungen in eine Datenbank ablagern. Dafür werden wir JPA und eine in-memory Datenbank (H2) verwenden.

#### 3.1 Datenbank anbinden und mit Daten befüllen

**Ziel:** Unsere H2 Datenbank wird mit dem Spring Boot server gestartet und mit Daten befüllt, und wir können diese in der H2 Console abfragen.

- Abhängigkeit auf Spring Data JPA und H2 in Maven hinzufügen: `org.springframework.boot:spring-boot-starter-data-jpa` und `com.h2database:h2`

Mit dem Spring-Data JPA starter wird automatisch versucht, eine passende Datenbank zu konfigurieren. Wenn H2 sich auf dem Classpath befindet, wird per Default eine In-memory-Instanz gestartet.  

Die H2 Console ist standardmässig nicht aktiviert. Wir können sie per Konfiguration aktivieren: 

- Spring Configuration file in `src/main/resources` anlegen: `application.yml`
- Die Eigenschaft `spring.h2.console.enabled` auf `true` setzen

Jetzt ist die H2 Console verfügbar: http://localhost:8080/h2-console. Man kann sich mit folgenden Parameter an die DB verbinden: URL = `jdbc:h2:mem:testdb`, username = `sa`, leeres Passwort. 

Die in-memory Datenbank geht bei jedem Neustart des Servers verloren. Für diesen Workshop können wir mittels eines SQL-Skripts bei jedem Start die Daten befüllen.

- Skript mit Name `data.sql` in `src/main/resources` anlegen
- Folgendes SQL soll die Greetings-Daten anlegen:
  ```sql
  DROP TABLE IF EXISTS greetings;
  
  CREATE TABLE greetings (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    language VARCHAR(10) NOT NULL,
    text VARCHAR(250) NOT NULL
  );
  
  INSERT INTO greetings (language, text) VALUES
    ('en', 'Hello World!'),
    ('de', 'Hallo Welt!'),
    ('fr', 'Salut, monde !');
  ```

Nach einem Server Neustart sollten die Daten in der H2 Console ersichtlich sein.

#### 3.2 Daten lesen mit JPA 

**Ziel:** Ein Durchstich bauen, in dem wir das Begrüssungstext "Hello World!" aus der Datenbank holen.

- Entitätsklasse anlegen: `Greeting`
- Annotation `javax.persistence.Entity`, damit sie von Spring Data als JPA Entität erkannt wird
- Name der SQL Tabelle mit einer `javax.persistence.Table`-Annotation angeben
- Ein Feld pro SQL Spalte deklarieren, mit dem gleichen Name wie die SQL Spalte. Getters/setters generieren
- Das "id" Feld mit `javax.persistence.Id` annotieren

Wir haben jetzt unser Object-Relational Mapping. Wir brauchen jetzt ein `Repository`, der sich um das Querying aus der DB kümmert.

- Interface anlegen `GreetingRepository`
- Spring Datas `JpaRepository` erweitern, um standard CRUD-Funktionalität zu erhalten. Entität und ID-Klasse als Generic Types eingeben.
- Im `GreetingService` eine Abhängigkeit zum `GreetingRepository` einbauen
- In der Service-Methode `greetingRepository.findById()` verwenden, um die Begrüssung mit `id = 1` zu holen und deren Text zurückliefern (als Durchstich)

Mit einem Server Neustart können wir sehen, dass die Texte jetzt aus der Datenbank geholt werden. Mit einem Debugger können wir andere Methoden des Repositories probieren (`findAll`...).

#### 3.3 Eigene Queries mit Derived Query Methods ausführen

**Ziel:** Beim REST-Aufruf spezifizieren können, auf welche Sprache ich meine Begrüssung erhalten möchte. Z.B. als Request Parameter: `GET /greeting?language=de`

Wenn ich in meinem Repository Interface methoden deklariere, die bestimmte Keywords entsprechen, kann Spring Data diese Keywords automatisch erkennen und eine Implementation liefern. Ich kann deklarativ meine Queries auflisten und sie werden automatisch aufgebaut und ausgeführt.

- Im `GreetingRepository` eine Methode deklarieren: `findByLanguage`. Ein return type von `Optional<Greeting>` ist für Suchen nach *einem* Wert empfohlen.
- Die Methode in `GreetingService` verwenden
- Parameter `language` extrahieren, bis zur Controller-Methode
- Controller-Parameter mit `@RequestParam` annotieren
- Ein `defaultValue` von `en` konfigurieren

Die Sprache der Begrüssung kann jetzt aus der API geändert werden.

Danke fürs Mitmachen 😊

## Referenzen

- Spring Boot [Getting started guide](https://spring.io/guides/gs/spring-boot/)
- [Spring Boot Dokumentation](https://docs.spring.io/spring-boot/docs/2.3.0.BUILD-SNAPSHOT/reference/htmlsingle/)
- Eine [Liste von starters](https://docs.spring.io/spring-boot/docs/2.3.0.BUILD-SNAPSHOT/reference/htmlsingle/#using-boot-starter)
- Tutorial zu [Spring Data Derived Queries](https://www.baeldung.com/spring-data-derived-queries)

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
- Server starten: `mvn spring-boot:run`. Cooles ASCII-Art erwarten.

#### 1.2 Controller hinzufügen

Bis jetzt läuft auf dem Server nichts. Wir müssen einen REST-Endpoint bauen.

REST-Endpoints werden in sogenannten `Controller`s definiert. Vermutlich kommt das aus dem MVC-Pattern, auf dem Spring Web-MVC aufgebaut wurde. Sie sind die Spring-Äquivalenten zu Java EE `Resource`-Klassen.

- Controller-Klasse hinzufügen (evtl im eigenen Package): `GreetingController`
- Mit `@RestController` annotieren
- Eine Methode hinzufügen, die die gewünschte Begrüssung als String zurückgibt
- Methode mit `@RequestMapping` annotieren, so dass HTTP `GET` requests auf dem Pfad `/greeting` zu dieser Methode dispatched werden.

Nach einem Neustart des Servers sollte unsere Hello World REST API funktionieren.

#### Production-Ready Hello World

Mit einem zusätzlichen Starter können wir unserer REST API Observability geben: Actuator.

- Actuator dependency hinzufügen: `spring-boot-starter-actuator`

Mit einem `GET /actuator` erhalten wir eine Liste von Metriken. Mit z.B. `GET /actuator/health` können wir prüfen, ob der Server erforlgreich hochgefahren ist.

## 2. Testing

?

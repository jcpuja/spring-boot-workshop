package ch.bluesky.spring.controller;

import ch.bluesky.spring.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private final GreetingService greetingService;

  public GreetingController(GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @GetMapping(path = "greeting")
  public String sayHello() {
    return greetingService.getGreeting();
  }

}

package ch.bluesky.spring.controller;

import ch.bluesky.spring.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GreetingController {

  private final GreetingService greetingService;

  @GetMapping(path = "greeting")
  public String sayHello(@RequestParam(defaultValue = "en") String language) {
    return greetingService.getGreeting(language);
  }

}

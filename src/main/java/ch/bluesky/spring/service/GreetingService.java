package ch.bluesky.spring.service;

import ch.bluesky.spring.persistence.Greeting;
import ch.bluesky.spring.persistence.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingService {

  private final GreetingRepository greetingRepository;

  public String getGreeting(String language) {
    return greetingRepository.findByLanguage(language)
        .map(Greeting::getText)
        .orElse("");
  }

}

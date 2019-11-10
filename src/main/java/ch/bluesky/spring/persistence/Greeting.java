package ch.bluesky.spring.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "GREETINGS")
public class Greeting {

  @Id
  private Long id;
  private String language;
  private String text;
}

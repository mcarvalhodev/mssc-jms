package guru.springframework.msscjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelloWorldMessage {

  static final long serialVersionUID = 7713367429426050793L;

  private UUID id;
  private String message;
}

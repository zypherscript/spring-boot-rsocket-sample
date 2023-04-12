package com.example.demo;

import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.retry.Retry;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  CommandLineRunner runner(RSocketRequester rSocketRequester) {
    return args -> {
      rSocketRequester
          .route("request-response")
          .data(new Message("Client", "Request"))
          .retrieveMono(Message.class)
          .subscribe(System.out::println);
    };
  }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Message {

  private String origin;
  private String interaction;
  private long index;
  private long created = Instant.now().getEpochSecond();

  public Message(String origin, String interaction) {
    this.origin = origin;
    this.interaction = interaction;
    this.index = 0;
  }

  public Message(String origin, String interaction, long index) {
    this.origin = origin;
    this.interaction = interaction;
    this.index = index;
  }
}

@Controller
@Slf4j
class RSocketController {

  static final String SERVER = "Server";
  static final String RESPONSE = "Response";

  @MessageMapping("request-response")
  Message requestResponse(Message request) {
    log.info("Received request-response request: {}", request);
    // create a single Message and return it
    return new Message(SERVER, RESPONSE);
  }
}

@Configuration
class ClientConfiguration {

  @Bean
  public RSocketRequester getRSocketRequester(RSocketRequester.Builder builder) {

    return builder
        .rsocketConnector(
            rSocketConnector ->
                rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)))
        )
        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
        .tcp("localhost", 9797);
  }
}

@RestController
@RequiredArgsConstructor
class MessageRestController {

  private final RSocketRequester rSocketRequester;

  @GetMapping(value = "/message/{origin}/{interaction}")
  public Publisher<Message> message(@PathVariable String origin,
      @PathVariable String interaction) {
    return rSocketRequester
        .route("request-response")
        .data(new Message(origin, interaction))
        .retrieveMono(Message.class);
  }
}

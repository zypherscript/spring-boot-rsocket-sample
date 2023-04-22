package com.example.demo;

import com.example.demo.model.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  @ConditionalOnProperty(name = "service.test", havingValue = "true")
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

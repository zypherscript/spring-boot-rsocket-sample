package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.model.Message;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketRequester.RequestSpec;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest(MessageRestController.class)
class MessageRestControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private RSocketRequester rSocketRequester;

  @Mock
  private RequestSpec requestSpec;

  @Test
  void testMessage() {
    var messageResponse = new Message("Server", "Response");
    when(rSocketRequester.route("request-response")).thenReturn(requestSpec);
    when(requestSpec.data(any())).thenReturn(requestSpec);
    when(requestSpec.retrieveMono(Message.class)).thenReturn(Mono.just(messageResponse));

    var result = webTestClient.get()
        .uri("/message/{origin}/{interaction}", "Client", "Request")
        .accept(MediaType.parseMediaType("text/event-stream;charset=UTF-8"))
        .exchange()
        .expectStatus().isOk()
        .returnResult(Message.class);
    StepVerifier.create(result.getResponseBody())
        .expectNext(messageResponse)
        .expectComplete()
        .verify();
  }
}
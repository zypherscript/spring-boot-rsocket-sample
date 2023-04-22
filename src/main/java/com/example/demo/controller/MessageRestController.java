package com.example.demo.controller;

import com.example.demo.model.Message;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record MessageRestController(RSocketRequester rSocketRequester) {

  @GetMapping(value = "/message/{origin}/{interaction}")
  public Publisher<Message> message(@PathVariable String origin,
      @PathVariable String interaction) {
    return rSocketRequester
        .route("request-response")
        .data(new Message(origin, interaction))
        .retrieveMono(Message.class);
  }
}

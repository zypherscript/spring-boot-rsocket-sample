package com.example.demo.rsocketserver.controller;

import com.example.demo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class RSocketController {

  static final String SERVER = "Server";
  static final String RESPONSE = "Response";

  @MessageMapping("request-response")
  Message requestResponse(Message request) {
    log.info("Received request-response request: {}", request);
    // create a single Message and return it
    return new Message(SERVER, RESPONSE);
  }
}

package com.example.demo.rsocketserver.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.Message;
import org.junit.jupiter.api.Test;

class RSocketControllerTest {

  @Test
  public void testGreet() {
    var message = new Message("Client", "Request");
    var result = new RSocketController().requestResponse(message);
    assertThat(result.getOrigin()).isEqualTo("Server");
    assertThat(result.getInteraction()).isEqualTo("Response");
  }
}
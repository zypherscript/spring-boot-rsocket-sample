package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.controller.MessageRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

  @Autowired
  private MessageRestController messageRestController;

  @Test
  void contextLoads() {
    assertThat(messageRestController).isNotNull();
  }

}

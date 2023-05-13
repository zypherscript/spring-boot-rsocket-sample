package com.example.demo.model;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {

  private String origin;
  private String interaction;
  private long index;
  private long created = Instant.now().getEpochSecond();

  public Message(String origin, String interaction) {
    this.origin = origin;
    this.interaction = interaction;
    this.index = 0;
  }
}

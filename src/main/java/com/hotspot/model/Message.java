package com.hotspot.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "messages")
public class Message {
   @Id
   private Integer id;
   private String content;
   private LocalDateTime timeSent;
   private Integer userId;
   private Integer chatId;
}

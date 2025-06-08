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
   private String id;
   private String content;
   private LocalDateTime timeSent;
   private String userId;
   private String chatId;

   public Message(String content, LocalDateTime timeSent, String userId, String chatId) {
      this.content = content;
      this.timeSent = timeSent;
      this.userId = userId;
      this.chatId = chatId;
   }
}

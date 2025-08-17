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
   private String senderId;
   private String chatId;

   public Message(String content, LocalDateTime timeSent, String senderId, String chatId) {
      this.content = content;
      this.timeSent = timeSent;
      this.senderId = senderId;
      this.chatId = chatId;
   }
}

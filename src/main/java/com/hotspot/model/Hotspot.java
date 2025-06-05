package com.hotspot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "hotspots")
public class Hotspot {
   @Id
   private String id;
   // need to add location
   private boolean active = false;
   private String chatId;
   private Integer upvotes = 0;
   private Integer downvotes = 0;

   public Hotspot(String chatId) {
      this.chatId = chatId;
   }

}

package com.hotspot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "hotspots")
public class Hotspot {

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class Coordinates {
      private Double latitude;
      private Double longitude;
   }

   @Id
   private String id;
   private Coordinates location;
   private boolean active = false;
   private String chatId;
   private Integer upvotes = 0;
   private Integer downvotes = 0;

   public Hotspot(String chatId, Double latitude, Double longitude) {
      this.chatId = chatId;
      this.location = new Coordinates(latitude, longitude);
   }

}

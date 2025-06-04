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
   private boolean active;
   private String chatId;
   private Integer upvotes;
   private Integer downvotes;

   
}

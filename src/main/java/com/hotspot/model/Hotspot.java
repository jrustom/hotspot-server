package com.hotspot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection="hotspots")
public class Hotspot {
   @Id
   private Integer id;
   private boolean active; 
   private Integer chatId;
}

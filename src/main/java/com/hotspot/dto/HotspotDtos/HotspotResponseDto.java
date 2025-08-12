package com.hotspot.dto.HotspotDtos;

import com.hotspot.model.Hotspot;
import com.hotspot.model.Hotspot.Coordinates;

import lombok.Getter;

@Getter
public class HotspotResponseDto {
    private final String id;
    private final boolean active;
    private final String chatId;
    private final Integer upvotes;
    private final Integer downvotes;
    private final Coordinates location;

    public HotspotResponseDto(Hotspot hotspot) {
        this.id = hotspot.getId();
        this.active = hotspot.isActive();
        this.chatId = hotspot.getChatId();
        this.upvotes = hotspot.getUpvotes();
        this.downvotes = hotspot.getDownvotes();
        this.location = hotspot.getLocation();
    }
}

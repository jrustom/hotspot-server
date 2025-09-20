package com.hotspot.dto.HotspotDtos;

import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.model.Hotspot;
import lombok.Getter;


@Getter
public class HotspotVoteResponseDto {
        private final String id;
        private final String name;
        private final boolean active;
        private final String chatId;
        private final Integer upvotes;
        private final Integer downvotes;
        private final Hotspot.Coordinates location;
        private final AccountResponseDto votingUser;

        public HotspotVoteResponseDto(Hotspot hotspot,
                                      AccountResponseDto votingUser) {
            this.id = hotspot.getId();
            this.name = hotspot.getName();
            this.active = hotspot.isActive();
            this.chatId = hotspot.getChatId();
            this.upvotes = hotspot.getUpvotes();
            this.downvotes = hotspot.getDownvotes();
            this.location = hotspot.getLocation();
            this.votingUser = votingUser;
        }
}

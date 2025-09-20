package com.hotspot.services;

import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.dto.HotspotDtos.HotspotVoteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hotspot.dto.ChatDtos.ChatResponseDto;
import com.hotspot.dto.HotspotDtos.HotspotRequestDto;
import com.hotspot.dto.HotspotDtos.HotspotResponseDto;
import com.hotspot.exceptions.ErrorCode;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.Hotspot;
import com.hotspot.model.User;
import com.hotspot.model.User.VoteType;
import com.hotspot.repositories.HotspotRepository;
import com.hotspot.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// This service is responsible for actions relating to a hotspot: creating one,
// voting for one, joining one, etc
@Service
@RequiredArgsConstructor
public class HotspotService {
    private final HotspotRepository hotspotRepo;
    private final UserRepository userRepo;
    private final AccountService accountService;
    private final ChatService chatService;
    private final MongoTemplate mongoTemplate;

    private Hotspot findHotspot(String id) {
        return hotspotRepo.findById(id)
                .orElseThrow(
                        () -> new HotspotException(ErrorCode.HOTSPOT_NOT_FOUND, "This hotspot does not exist"));
    }

    public List<HotspotResponseDto> getHotspots() {
        return hotspotRepo.findAll().stream().map(HotspotResponseDto::new).collect(Collectors.toList());
    }

    public HotspotResponseDto getHotspot(String id) {
        return new HotspotResponseDto(findHotspot(id));
    }

    public HotspotResponseDto createHotspot(HotspotRequestDto request) {
        // Create chat
        ChatResponseDto newChat = chatService.createChat();

        // Create hotspot
        Hotspot newHotspot = new Hotspot(request.getName(), newChat.getId(),
                request.getLat(), request.getLng());

        return new HotspotResponseDto(hotspotRepo.save(newHotspot));
    }

    public HotspotVoteResponseDto vote(VoteType voteType, String hotspotId,
                                       String voterId) {
        // Find hotspot and user
        Hotspot hotspotToVote = findHotspot(hotspotId);
        User votingUser = accountService.findUser(voterId);

        if (hotspotToVote.isActive()) {
            throw new HotspotException(ErrorCode.HOTSPOT_INVALID_VOTE, "Cannot vote for an active hotspot");
        }

        // Check if user has already voted
        VoteType existingVote = votingUser.getVoteRecords().get(hotspotId);

        if (null != existingVote) {
            switch (existingVote) {
                case VoteType.UPVOTE -> hotspotToVote.setUpvotes(hotspotToVote.getUpvotes() - 1);
                case VoteType.DOWNVOTE -> hotspotToVote.setDownvotes(hotspotToVote.getDownvotes() - 1);
            }
        }

        switch (voteType) {
            case VoteType.UPVOTE -> hotspotToVote.setUpvotes(hotspotToVote.getUpvotes() + 1);
            case VoteType.DOWNVOTE -> hotspotToVote.setDownvotes(hotspotToVote.getDownvotes() + 1);
        }
        votingUser.addVoteRecord(hotspotId, voteType);

        User updatedUser = userRepo.save(votingUser);
        return new HotspotVoteResponseDto(hotspotRepo.save(hotspotToVote),
                new AccountResponseDto(updatedUser));
    }

    public HotspotVoteResponseDto cancelVote(VoteType voteType, String hotspotId
            , String voterId) {
        // Find hotspot and user
        Hotspot hotspotToUpdate = findHotspot(hotspotId);
        User votingUser = accountService.findUser(voterId);

        VoteType existingVote = votingUser.getVoteRecords().get(hotspotId);

        if (existingVote != voteType) {
            throw new HotspotException(ErrorCode.HOTSPOT_INVALID_VOTE,
                    "This user has not voted for this hotspot yet, cannot cancel it");
        }

        switch (voteType) {
            case VoteType.UPVOTE -> hotspotToUpdate.setUpvotes(hotspotToUpdate.getUpvotes() - 1);
            case VoteType.DOWNVOTE -> hotspotToUpdate.setDownvotes(hotspotToUpdate.getDownvotes() - 1);
        }

        votingUser.removeVoteRecord(hotspotId, voteType);

        User updatedUser = userRepo.save(votingUser);
        return new HotspotVoteResponseDto(hotspotRepo.save(hotspotToUpdate),
                new AccountResponseDto(updatedUser));
    }

    public HotspotResponseDto activate(String hotspotId) {
        // Find hotspot
        Hotspot hotspotToActivate = findHotspot(hotspotId);

        hotspotToActivate.setActive(true);

        // Remove the vote record for this hotspot from all users
        Update update = new Update().unset("voteRecords." + hotspotId);
        mongoTemplate.updateMulti(new Query(), update, User.class);

        return new HotspotResponseDto(hotspotRepo.save(hotspotToActivate));
    }

}

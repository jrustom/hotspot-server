package com.hotspot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hotspot.dto.ChatDtos.ChatResponseDto;
import com.hotspot.dto.HotspotDtos.HotspotResponseDto;
import com.hotspot.exceptions.ErrorCode;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.Hotspot;
import com.hotspot.model.User;
import com.hotspot.model.User.VoteType;
import com.hotspot.repositories.HotspotRepository;
import com.hotspot.repositories.UserRepository;
import com.mongodb.BasicDBObject;

// This service is responsible for actions relating to a hotspot: creating one,
// voting for one, joining one, etc
@Service
public class HotspotService {
    private HotspotRepository hotspotRepo;
    private UserRepository userRepo;
    private AccountService accountService;
    private ChatService chatService;
    private MongoTemplate mongoTemplate;

    @Autowired
    public HotspotService(HotspotRepository hotspotRepo, UserRepository userRepo, AccountService accountService,
            ChatService chatService,
            MongoTemplate mongoTemplate) {
        this.hotspotRepo = hotspotRepo;
        this.userRepo = userRepo;
        this.accountService = accountService;
        this.mongoTemplate = mongoTemplate;
        this.chatService = chatService;
    }

    private Hotspot findHotspot(String id) {
        return hotspotRepo.findById(id)
                .orElseThrow(
                        () -> new HotspotException(ErrorCode.HOTSPOT_NOT_FOUND, "This hotspot does not exist"));
    }

    public HotspotResponseDto getHotspot(String id) {
        return new HotspotResponseDto(findHotspot(id));
    }

    public HotspotResponseDto createHotspot() {
        // Create chat
        ChatResponseDto newChat = chatService.createChat();

        // Create hotspot
        Hotspot newHotspot = new Hotspot(newChat.getId());

        return new HotspotResponseDto(hotspotRepo.save(newHotspot));
    }

    public HotspotResponseDto vote(VoteType voteType, String hotspotId, String voterId) {
        // Find hotspot and user
        Hotspot hotspotToVote = findHotspot(hotspotId);
        User votingUser = accountService.findUser(voterId);

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

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToVote));
    }

    public HotspotResponseDto cancelVote(VoteType voteType, String hotspotId, String voterId) {
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

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    }

    public HotspotResponseDto activate(String hotspotId) {
        // Find hotspot
        Hotspot hotspotToActivate = findHotspot(hotspotId);

        hotspotToActivate.setActive(true);
        // Remove votes from users
        mongoTemplate.updateMulti(Query.query(Criteria.where("hotspotId").is(hotspotId)),
                new Update().pull("voteRecords", new BasicDBObject("hotspotId", hotspotId)), User.class);

        return new HotspotResponseDto(hotspotRepo.save(hotspotToActivate));
    }

}

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
import com.hotspot.model.User.VoteRecord;
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
        Hotspot hotspotToUpvote = findHotspot(hotspotId);
        User votingUser = accountService.findUser(voterId);

        switch (voteType) {
            case VoteType.UPVOTE -> hotspotToUpvote.setUpvotes(hotspotToUpvote.getUpvotes() + 1);
            case VoteType.DOWNVOTE -> hotspotToUpvote.setDownvotes(hotspotToUpvote.getDownvotes() + 1);
        }
        votingUser.addVoteRecord(new VoteRecord(hotspotId, voteType));

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpvote));
    }

    // TODO: consider which methods should be @transactional, and what this
    // annotation means for mongodb/when it can be used with mongodb
    // public HotspotResponseDto upVote(String hotspotId, String voterId) {
    // // Find hotspot and user
    // Hotspot hotspotToUpvote = findHotspot(hotspotId);
    // User votingUser = accountService.findUser(voterId);

    // hotspotToUpvote.setUpvotes(hotspotToUpvote.getUpvotes() + 1);
    // votingUser.addVoteRecord(new VoteRecord(hotspotId, VoteType.UPVOTE));

    // userRepo.save(votingUser);
    // return new HotspotResponseDto(hotspotRepo.save(hotspotToUpvote));
    // }

    // public HotspotResponseDto downVote(String hotspotId, String voterId) {
    // // Find hotspot and user
    // Hotspot hotspotToDownvote = findHotspot(hotspotId);
    // User votingUser = accountService.findUser(voterId);

    // hotspotToDownvote.setDownvotes(hotspotToDownvote.getDownvotes() + 1);
    // votingUser.addVoteRecord(new VoteRecord(hotspotId, VoteType.DOWNVOTE));

    // userRepo.save(votingUser);
    // return new HotspotResponseDto(hotspotRepo.save(hotspotToDownvote));
    // }

    public HotspotResponseDto cancelVote(VoteType voteType, String hotspotId, String voterId) {
        // Find hotspot and user
        Hotspot hotspotToUpdate = findHotspot(hotspotId);
        User votingUser = accountService.findUser(voterId);

        // Only continue if this person has already voted accordingly
        VoteRecord previousVote = votingUser.getVoteRecords().stream()
                .filter(voteRecord -> voteRecord.getHotspotId().equals(hotspotId)
                        && voteRecord.getVoteType().equals(voteType))
                .findFirst().orElseThrow(() -> new HotspotException(ErrorCode.HOTSPOT_INVALID_VOTE,
                        "This user has not voted for this hotspot yet, cannot cancel it"));

        switch (voteType) {
            case VoteType.UPVOTE -> hotspotToUpdate.setUpvotes(hotspotToUpdate.getUpvotes() - 1);
            case VoteType.DOWNVOTE -> hotspotToUpdate.setDownvotes(hotspotToUpdate.getDownvotes() - 1);
        }
        votingUser.removeVoteRecord(previousVote);

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    }

    // This can only be called if the user has upvoted though - we need a way to
    // persist votes anyways so a user needs to store what hotspots he's voted for -
    // also when a hotspot becomes active, need to remove it from the voted list of
    // all users that voted for it - use a $pull operator
    // public HotspotResponseDto cancelUpVote(String hotspotId, String voterId) {
    // // Find hotspot and user
    // Hotspot hotspotToUpdate = findHotspot(hotspotId);
    // User votingUser = accountService.findUser(voterId);

    // // Only continue if this person has upvoted
    // VoteRecord previousVote = votingUser.getVoteRecords().stream()
    // .filter(voteRecord -> voteRecord.getHotspotId().equals(hotspotId)
    // && voteRecord.getVoteType().equals(VoteType.UPVOTE))
    // .findFirst().orElseThrow(() -> new
    // HotspotException(ErrorCode.HOTSPOT_INVALID_VOTE,
    // "This user has not upvoted for this hotspot yet, cannot cancel it"));

    // hotspotToUpdate.setUpvotes(hotspotToUpdate.getUpvotes() - 1);
    // votingUser.removeVoteRecord(previousVote);

    // userRepo.save(votingUser);
    // return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    // }

    // public HotspotResponseDto cancelDownVote(String hotspotId, String voterId) {
    // // Find hotspot and user
    // Hotspot hotspotToUpdate = findHotspot(hotspotId);
    // User votingUser = accountService.findUser(voterId);

    // // Only continue if this person has upvoted
    // VoteRecord previousVote = votingUser.getVoteRecords().stream()
    // .filter(voteRecord -> voteRecord.getHotspotId().equals(hotspotId)
    // && voteRecord.getVoteType().equals(VoteType.DOWNVOTE))
    // .findFirst().orElseThrow(() -> new
    // HotspotException(ErrorCode.HOTSPOT_INVALID_VOTE,
    // "This user has not downvoted for this hotspot yet, cannot cancel it"));

    // hotspotToUpdate.setUpvotes(hotspotToUpdate.getDownvotes() - 1);
    // votingUser.removeVoteRecord(previousVote);

    // userRepo.save(votingUser);
    // return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    // }

    // need to remove from all lists that voted for it
    // TODO: IT MIGHT BE A BAD IDEA TO HAVE HTTP STATUS IN THE SERVICE, IT SHOULD BE
    // SEPERATE, FIND A WAY AROUND THIS
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

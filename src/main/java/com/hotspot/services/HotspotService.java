package com.hotspot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hotspot.dto.HotspotDtos.HotspotResponseDto;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.Hotspot;
import com.hotspot.model.User;
import com.hotspot.model.User.VoteRecord;
import com.hotspot.model.User.VoteType;
import com.hotspot.repositories.HotspotRepository;
import com.hotspot.repositories.UserRepository;

@Service
// This service is responsible for actions relating to a hotspot: creating one,
// voting for one, joining one, etc
public class HotspotService {
    private HotspotRepository hotspotRepo;
    private UserRepository userRepo;
    private AccountService accountService;

    @Autowired
    public HotspotService(HotspotRepository hotspotRepo, UserRepository userRepo, AccountService accountService) {
        this.hotspotRepo = hotspotRepo;
        this.userRepo = userRepo;
        this.accountService = accountService;
    }

    private Hotspot findHotspot(String id, Integer errorCode) {
        return hotspotRepo.findById(id)
                .orElseThrow(
                        () -> new HotspotException(HttpStatus.NOT_FOUND, errorCode, "This hotspot does not exist"));
    }

    // TODO: consider which methods should be @transactional, and what this
    // annotation means for mongodb/when it can be used with mongodb
    public HotspotResponseDto upVote(String hotspotId, String voterId) {
        // Find hotspot and user
        Hotspot hotspotToUpdate = findHotspot(hotspotId, 100);
        User votingUser = accountService.findUser(voterId, 100);

        hotspotToUpdate.setUpvotes(hotspotToUpdate.getUpvotes() + 1);
        votingUser.addVoteRecord(new VoteRecord(hotspotId, VoteType.UPVOTE));

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    }

    // This can only be called if the user has upvoted though - we need a way to
    // persist votes anyways so a user needs to store what hotspots he's voted for -
    // also when a hotspot becomes active, need to remove it from the voted list of
    // all users that voted for it - use a $pull operator
    public HotspotResponseDto cancelUpVote(String hotspotId, String voterId) {
        // Find hotspot and user
        Hotspot hotspotToUpdate = findHotspot(hotspotId, 100);
        User votingUser = accountService.findUser(voterId, 100);

        // Only continue if this person has upvoted
        VoteRecord previousVote = votingUser.getVoteRecords().stream()
                .filter(voteRecord -> voteRecord.getHotspotId().equals(hotspotId)
                        && voteRecord.getVoteType().equals(VoteType.UPVOTE))
                .findFirst().orElseThrow(() -> new HotspotException(HttpStatus.BAD_REQUEST, 100,
                        "This user has not upvoted for this hotspot yet, cannot cancel it"));

        hotspotToUpdate.setUpvotes(hotspotToUpdate.getUpvotes() - 1);
        votingUser.removeVoteRecord(previousVote);

        userRepo.save(votingUser);
        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    }

    public HotspotResponseDto downVote(String hotspotId) {
        // Find hotspot
        Hotspot hotspotToUpdate = findHotspot(hotspotId, 100);

        hotspotToUpdate.setDownvotes(Math.max(0, hotspotToUpdate.getDownvotes()));

        return new HotspotResponseDto(hotspotRepo.save(hotspotToUpdate));
    }

    public HotspotResponseDto activate(String hotspotId) {
    }

}

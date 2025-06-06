package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.dto.HotspotDtos.HotspotResponseDto;
import com.hotspot.model.User.VoteType;
import com.hotspot.services.HotspotService;

@RestController
@RequestMapping("/hotspots")
public class HotspotController {
    private HotspotService hotspotService;

    @Autowired
    public HotspotController(HotspotService hotspotService) {
        this.hotspotService = hotspotService;
    }

    @GetMapping("/{id}")
    public HotspotResponseDto getHotspot(@PathVariable(name = "id") String hotspotId) {
        return hotspotService.getHotspot(hotspotId);
    }

    @PostMapping("")
    public HotspotResponseDto createHotspot() {
        return hotspotService.createHotspot();
    }

    @PutMapping("/upvotes/{hid}/{uid}")
    public HotspotResponseDto upVote(@PathVariable(name = "hid") String hotspotId,
            @PathVariable(name = "uid") String userId) {
        return hotspotService.vote(VoteType.UPVOTE, hotspotId, userId);
    }

    @PutMapping("/downvotes/{hid}/{uid}")
    public HotspotResponseDto downVote(@PathVariable(name = "hid") String hotspotId,
            @PathVariable(name = "uid") String userId) {
        return hotspotService.vote(VoteType.DOWNVOTE, hotspotId, userId);
    }

    @DeleteMapping("/upvotes/{hid}/{uid}")
    public HotspotResponseDto cancelUpVote(@PathVariable(name = "hid") String hotspotId,
            @PathVariable(name = "uid") String userId) {
        return hotspotService.cancelVote(VoteType.UPVOTE, hotspotId, userId);
    }

    @DeleteMapping("/downvotes/{hid}/{uid}")
    public HotspotResponseDto cancelDownVote(@PathVariable(name = "hid") String hotspotId,
            @PathVariable(name = "uid") String userId) {
        return hotspotService.cancelVote(VoteType.DOWNVOTE, hotspotId, userId);
    }

    @PutMapping("/{id}")
    public HotspotResponseDto activate(@PathVariable(name = "id") String hotspotId) {
        return hotspotService.activate(hotspotId);
    }
}

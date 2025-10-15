package com.hotspot.controller;

import com.hotspot.dto.HotspotDtos.HotspotRequestDto;
import com.hotspot.dto.HotspotDtos.HotspotResponseDto;
import com.hotspot.dto.HotspotDtos.HotspotVoteResponseDto;
import com.hotspot.model.User.VoteType;
import com.hotspot.services.HotspotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotspots")
public class HotspotController {
    private final HotspotService hotspotService;

    @GetMapping("/all")
    public List<HotspotResponseDto> getHotspots() {
        return hotspotService.getHotspots();
    }

    @PostMapping("")
    public HotspotResponseDto createHotspot(@RequestBody @Valid HotspotRequestDto request) {
        return hotspotService.createHotspot(request);
    }

    @PutMapping("/upvotes/{hid}")
    public HotspotVoteResponseDto upVote(@PathVariable(name = "hid") String hotspotId) {
        return hotspotService.vote(VoteType.UPVOTE, hotspotId);
    }

    @DeleteMapping("/upvotes/{hid}")
    public HotspotVoteResponseDto cancelUpVote(@PathVariable(name = "hid") String hotspotId) {
        return hotspotService.cancelVote(VoteType.UPVOTE, hotspotId);
    }

    @PutMapping("/{id}")
    public HotspotResponseDto activate(@PathVariable(name = "id") String hotspotId) {
        return hotspotService.activate(hotspotId);
    }
}

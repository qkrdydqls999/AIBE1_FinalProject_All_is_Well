package org.example.bookmarket.dm.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.dm.dto.*;
import org.example.bookmarket.dm.service.DmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmService dmService;

    @PostMapping("/channel")
    public DmChannelResponse createChannel(@RequestBody DmChannelRequest request) {
        return dmService.createChannel(request);
    }

    @GetMapping("/channels/{userId}")
    public List<DmChannelResponse> getUserChannels(@PathVariable Long userId) {
        return dmService.getChannelsForUser(userId);
    }

    @GetMapping("/channel/{channelId}/messages")
    public List<DirectMessageResponse> getMessages(@PathVariable Long channelId) {
        return dmService.getMessages(channelId);
    }
}
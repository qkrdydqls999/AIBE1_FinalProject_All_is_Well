package org.example.bookmarket.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.*;
import org.example.bookmarket.chat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/channel")
    @ResponseStatus(HttpStatus.CREATED) // 📌 REST 명세상 201 반환
    public ChatResponse createChannel(@RequestBody ChatRequest request) {
        return chatService.createChannel(request);
    }

    @GetMapping("/channels/{userId}")
    public List<ChatResponse> getUserChannels(@PathVariable Long userId) {
        return chatService.getUserChannels(userId);
    }

    @GetMapping("/channel/{channelId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable Long channelId) {
        return chatService.getMessages(channelId);
    }
}
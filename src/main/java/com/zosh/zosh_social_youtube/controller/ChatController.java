package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.ChatRequest;
import com.zosh.zosh_social_youtube.dto.response.ChatResponse;
import com.zosh.zosh_social_youtube.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatController {

    ChatService chatService;

    // Tạo cuộc trò chuyện mới
    @PostMapping
    public ApiResponse<ChatResponse> createChat(@RequestBody ChatRequest request) {
        return ApiResponse.<ChatResponse>builder()
                .result(chatService.createChat(request))
                .build();
    }

    // Tìm cuộc trò chuyện theo ID
    @GetMapping("/{chatId}")
    public ApiResponse<ChatResponse> findChatById(@PathVariable String chatId) {
        return ApiResponse.<ChatResponse>builder()
                .result(chatService.findChatById(chatId))
                .build();
    }

    // Lấy tất cả cuộc trò chuyện của một User
    @GetMapping("/user/{userId}")
    public ApiResponse<List<ChatResponse>> findAllChatsByUserId(@PathVariable String userId) {
        return ApiResponse.<List<ChatResponse>>builder()
                .result(chatService.findUsersChat(userId))
                .build();
    }
}

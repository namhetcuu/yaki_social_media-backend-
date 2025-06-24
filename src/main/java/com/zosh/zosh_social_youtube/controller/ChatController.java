package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.ChatRequest;
import com.zosh.zosh_social_youtube.dto.response.ChatResponse;
import com.zosh.zosh_social_youtube.exception.AppException;
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

        log.info("Finding or creating chat for users: {}", request.getUserIds());
        ChatResponse chatResponse = chatService.findOrCreatePersonalChat(request.getUserIds().get(0), request.getUserIds().get(1), request.getChatImage());

        return ApiResponse.<ChatResponse>builder()
                .result(chatResponse)
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

    @DeleteMapping("/{chatId}")
    public ApiResponse<String> deleteChatById(@PathVariable String chatId) {
        log.info("Deleting chat with ID chat: {}", chatId);

        chatService.deleteChatById(chatId);
        return ApiResponse.<String>builder()
                .result("Chat has been deleted")
                .build();
    }

    // Xoá tất cả các cuộc trò chuyện của một user
//    @DeleteMapping("/users/{userId}")
//    public ApiResponse<String> deleteAllChatsByUserId(@PathVariable String userId) {
//        log.info("Deleting all chats for user ID: {}", userId);
//        chatService.deleteAllChatsByUserId(userId);
//        return ApiResponse.<String>builder()
//                .message("All chats of user deleted successfully")
//                .build();
//    }
}

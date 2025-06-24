package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.MessageRequest;
import com.zosh.zosh_social_youtube.dto.response.MessageResponse;
import com.zosh.zosh_social_youtube.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageController {

    MessageService messageService;
    SimpMessagingTemplate messagingTemplate; // Để gửi message qua WebSocket

    /**
     * API tạo tin nhắn mới qua REST và broadcast qua WebSocket.
     */
    @PostMapping
    public ApiResponse<MessageResponse> createMessage(@RequestBody MessageRequest request) {
        log.info("Create message with for chatId: " + request.getChatId());

        // Tạo message qua service
        MessageResponse messageResponse = messageService.createMessage(request);

        // Broadcast qua WebSocket tới /topic/messages
        messagingTemplate.convertAndSend("/topic/messages", messageResponse);

        return ApiResponse.<MessageResponse>builder()
                .result(messageResponse)
                .build();
    }

    /**
     * API lấy danh sách tin nhắn trong một chat cụ thể.
     */
    @GetMapping("/chat-messages/{chatId}")
    public ApiResponse<List<MessageResponse>> findChatsMessages(@PathVariable String chatId) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.findChatsMessages(chatId))
                .build();
    }

    /**
     * Xử lý tin nhắn gửi qua WebSocket (nếu frontend gửi trực tiếp).
     */
    @MessageMapping("/sendMessage")
    public void handleWebSocketMessage(MessageRequest request) {
        log.info("Received WebSocket message for chatId: " + request.getChatId());

        // Tạo message qua service
        MessageResponse messageResponse = messageService.createMessage(request);

        // Broadcast tới /topic/messages
        messagingTemplate.convertAndSend("/topic/messages", messageResponse);
    }
}
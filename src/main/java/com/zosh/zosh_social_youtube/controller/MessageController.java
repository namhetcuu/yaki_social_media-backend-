package com.zosh.zosh_social_youtube.controller;


import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.ChatRequest;
import com.zosh.zosh_social_youtube.dto.request.MessageRequest;
import com.zosh.zosh_social_youtube.dto.response.MessageResponse;
import com.zosh.zosh_social_youtube.service.ChatService;
import com.zosh.zosh_social_youtube.service.MessageService;
import com.zosh.zosh_social_youtube.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageController {

    MessageService messageService;
    UserService userService;
    ChatService chatService;


    /**
     * API tạo tin nhắn mới.
     */
    @PostMapping
    public ApiResponse<MessageResponse> createMessage(@RequestBody MessageRequest request){
        log.info("Create message with for chatId: " + request.getChatId());

        return ApiResponse.<MessageResponse>builder()
                .result(messageService.createMessage(request))
                .build();
    }

    /**
     * API lấy danh sách tin nhắn trong một chat cụ thể.
     */

    @GetMapping("chat-messages/{chatId}")
    public ApiResponse<List<MessageResponse>> findChatsMessages(@PathVariable String chatId){

        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.findChatsMessages(chatId))
                .build();

    }

}

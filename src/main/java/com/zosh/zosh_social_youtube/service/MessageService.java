package com.zosh.zosh_social_youtube.service;


import com.zosh.zosh_social_youtube.dto.request.MessageRequest;
import com.zosh.zosh_social_youtube.dto.response.MessageResponse;
import com.zosh.zosh_social_youtube.entity.Chat;
import com.zosh.zosh_social_youtube.entity.Message;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.mapper.MessageMapper;
import com.zosh.zosh_social_youtube.repository.ChatRepository;
import com.zosh.zosh_social_youtube.repository.MessageRepository;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageService {

    MessageRepository messageRepository;
    ChatRepository chatRepository;
    UserRepository userRepository;
    MessageMapper messageMapper;

    public MessageResponse createMessage(MessageRequest messageRequest) {

        log.info("Tạo tin nhắn mới từ userId={} trong chatId={}", messageRequest.getUserId(), messageRequest.getChatId());

        // Lấy user từ DB
        User user = userRepository.findById(messageRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + messageRequest.getUserId()));

        // Lấy chat từ DB
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chat với ID: " + messageRequest.getChatId()));

        // Tạo Message entity từ request
        Message message = Message.builder()
                .content(messageRequest.getContent())
                .image(messageRequest.getImage())
                .user(user)
                .chat(chat)
                .build();

        // Lưu message vào DB
        Message savedMessage = messageRepository.save(message);
        log.info("Đã lưu tin nhắn với ID: {}", savedMessage.getId());

        // Trả về MessageResponse
        return messageMapper.toMessageResponse(savedMessage);

    }

    @Transactional(readOnly = true)
    //Lấy danh sách tin nhắn theo chatId.
    public List<MessageResponse> findChatsMessages(String chatId){

        log.info("Lấy danh sách tin nhắn trong chatId={}", chatId);

        // Kiểm tra chat có tồn tại không
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chat với ID: " + chatId));

        // Lấy danh sách tin nhắn theo chatId
        List<Message> messages = messageRepository.findByChatId(chatId);

        // Chuyển đổi danh sách message thành response
        return messages.stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

}

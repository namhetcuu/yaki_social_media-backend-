package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.dto.request.ChatRequest;
import com.zosh.zosh_social_youtube.dto.response.ChatResponse;
import com.zosh.zosh_social_youtube.entity.Chat;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.exception.AppException;
import com.zosh.zosh_social_youtube.exception.ErrorCode;
import com.zosh.zosh_social_youtube.mapper.ChatMapper;
import com.zosh.zosh_social_youtube.repository.ChatRepository;
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
public class ChatService {

    ChatRepository chatRepository;
    ChatMapper chatMapper;
    UserRepository userRepository;

    @Transactional
    public ChatResponse createChat(ChatRequest chatRequest) {
        log.info("Tạo chat mới với tên: {}", chatRequest.getChatName());

        // Lấy danh sách user từ userId
        List<User> users = userRepository.findAllById(chatRequest.getUserIds());
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Tạo Chat entity
        Chat chat = Chat.builder()
                .chatName(chatRequest.getChatName())
                .chatImage(chatRequest.getChatImage())
                .users(users)
                .build();

        Chat savedChat = chatRepository.save(chat);
        log.info("Chat được tạo thành công với ID: {}", savedChat.getId());

        return chatMapper.toChatResponse(savedChat);
    }

    public ChatResponse findChatById(String chatId) {
        log.info("Tìm chat với ID: {}", chatId);

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_NOT_FOUND));

        return chatMapper.toChatResponse(chat);
    }

    public List<ChatResponse> findUsersChat(String userId) {
        log.info("🔎 Tìm danh sách chat của userId: {}", userId);

        // Kiểm tra user có tồn tại không, nếu không thì ném exception
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Truy vấn danh sách chat mà user tham gia
        List<Chat> chats = chatRepository.findByUsers_Id(userId);

        log.info("✅ Tìm thấy {} chat của userId: {}", chats.size(), userId);

        // Chuyển đổi danh sách Chat thành danh sách ChatResponse
        return chats.stream()
                .map(chatMapper::toChatResponse)
                .collect(Collectors.toList());
    }

    //Kiểm tra xem hai người dùng có chat chung không
    ///public Optional<Chat> findChatBetweenUsers(String userId1, String userId2);

    //Xóa một cuộc trò chuyện theo ID (chỉ admin hoặc chủ chat được phép).
    //public void deleteChat(String chatId, String requesterId);

    //Thêm người dùng vào cuộc trò chuyện
    //public ChatResponse addUserToChat(String chatId, String userId);

    //Xóa người dùng khỏi cuộc trò chuyện
    //public ChatResponse removeUserFromChat(String chatId, String userId);

    //Khi người dùng gửi một tin nhắn vào một cuộc trò chuyện.
    //public MessageResponse sendMessage(MessageRequest messageRequest);

}

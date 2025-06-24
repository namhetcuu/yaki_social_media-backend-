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
import org.springframework.security.access.prepost.PreAuthorize;
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
    UserService userService;

    @Transactional
    public ChatResponse findOrCreatePersonalChat(String userId1, String userId2, String chatImage) {
        log.info("Checking for existing personal chat between {} and {}", userId1, userId2);

        // Chuyển userId từ String -> User trước khi tìm chat
        User user1 = userService.findUserById(userId1);
        User user2 = userService.findUserById(userId2);

        // Kiểm tra nếu userId1 == userId2 (không cho phép chat với chính mình)
        if (user1.getId().equals(user2.getId())) {
            throw new IllegalArgumentException("A user cannot chat with themselves.");
        }

        // Kiểm tra xem cuộc trò chuyện đã tồn tại chưa
        List<Chat> existingChats = chatRepository.findPersonalChat(user1, user2);
        for (Chat chat : existingChats) {
            if (chat.getUsers().size() == 2) { // Chỉ lấy chat có đúng 2 người
                log.info("Existing personal chat found: {}", chat.getId());
                return chatMapper.toChatResponse(chat);
            }
        }

        // Nếu chưa có, tạo mới cuộc trò chuyện
        log.info("No existing chat found. Creating a new personal chat.");

        Chat newChat = Chat.builder()
                .chatName("Private Chat") // Hoặc đặt theo tên user
                .chatImage(chatImage)
                .users(List.of(user1, user2))
                .build();

        Chat savedChat = chatRepository.save(newChat);
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

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteChatById(String chatId) {
        log.info("Deleting user with ID: {}", chatId);
        chatRepository.deleteById(chatId);
    }

//    public void deleteAllChatsByUserId(Long userId) {
//
//    }


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

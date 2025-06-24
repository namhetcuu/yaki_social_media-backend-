package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Chat;
import com.zosh.zosh_social_youtube.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {

    //SIZE(c.users) = 2 → Chỉ lấy các cuộc trò chuyện có đúng 2 thành viên (chat cá nhân).
    //:userId1 MEMBER OF c.users → Kiểm tra userId1 có trong danh sách users của cuộc trò chuyện.
    //:userId2 MEMBER OF c.users → Kiểm tra userId2 có trong danh sách users.
    //Trả về danh sách Chat, phòng trường hợp có nhiều cuộc trò chuyện trùng lặp.
    @Query("SELECT c FROM Chat c WHERE :user1 MEMBER OF c.users AND :user2 MEMBER OF c.users")
    List<Chat> findPersonalChat(@Param("user1") User user1, @Param("user2") User user2);







    // Tìm danh sách chat mà user tham gia dựa trên userId
    List<Chat> findByUsers_Id(String userId);

    // Tìm danh sách chat có chứa một user nhất định
    List<Chat> findByUsersContaining(User user);

    // Tìm chat theo ID và user trong danh sách thành viên
    Optional<Chat> findByIdAndUsersContaining(String chatId, User user);

    // Truy vấn chat dựa trên userId và chatId
    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.id = :userId AND c.id = :chatId")
    Optional<Chat> findChatByUserIdAndChatId(@Param("userId") String userId, @Param("chatId") String chatId);

    // Tìm danh sách chat giữa hai người dùng (tránh tạo trùng lặp chat cá nhân)
    @Query("SELECT c FROM Chat c JOIN c.users u1 JOIN c.users u2 WHERE u1.id = :userId1 AND u2.id = :userId2")
    Optional<Chat> findPrivateChatBetweenUsers(@Param("userId1") String userId1, @Param("userId2") String userId2);
}


package com.zosh.zosh_social_youtube.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    String content;  // Nội dung tin nhắn
    String image;    // Đường dẫn hình ảnh (nếu có)
    String userId;   // ID của người gửi tin nhắn
    String chatId;   // ID của cuộc trò chuyện

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}

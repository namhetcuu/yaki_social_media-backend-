package com.zosh.zosh_social_youtube.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRequest {

    String chatName;
    String chatImage;

    //@JsonProperty("userIds") giúp Spring Boot hiểu rằng trường
    // userIds trong JSON tương ứng với List<String> userIds trong Java.
    @JsonProperty("userIds")  // Giúp Spring Boot deserialize JSON đúng cách
    List<String> userIds;  // Danh sách ID của các user tham gia chat

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}

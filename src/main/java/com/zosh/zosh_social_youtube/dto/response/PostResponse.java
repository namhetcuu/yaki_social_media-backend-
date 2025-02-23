package com.zosh.zosh_social_youtube.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String caption;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int likeCount;
    String authorId;
    String authorUsername;
    Set<String> likedUsers; // Danh sách ID của những người đã thích bài viết (nếu cần)
    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
    public void setLikedUsers(Set<String> likedUsers) {
        this.likedUsers = likedUsers;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

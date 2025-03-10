package com.zosh.zosh_social_youtube.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
    String userId;  // ID của người bình luận
    String postId;  // ID của bài viết mà bình luận thuộc về
    String content; // Nội dung bình luận
}

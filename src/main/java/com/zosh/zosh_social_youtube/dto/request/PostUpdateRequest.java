package com.zosh.zosh_social_youtube.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdateRequest {

    @NotBlank(message = "Post ID không được để trống")
    String postId; // ID của bài viết cần cập nhật,
    // Bắt buộc, để xác định bài viết nào cần cập nhật

    @Size(max = 500, message = "Caption không được vượt quá 500 ký tự")
    String caption; // Nội dung mới của bài viết,Có thể null

    String imageUrl; // Link ảnh mới (có thể null nếu không muốn cập nhật)

    String videoUrl; // Link video mới (có thể null nếu không muốn cập nhật)
}

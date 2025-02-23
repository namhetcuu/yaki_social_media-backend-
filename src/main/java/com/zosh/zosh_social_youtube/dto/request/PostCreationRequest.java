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
public class PostCreationRequest {

    //@NotBlank(message = "Caption không được để trống")
    @Size(max = 500, message = "CAPTION_INVALID")
    String caption;

    String imageUrl; // Ảnh minh họa cho bài viết (có thể null)

    String videoUrl; // Video đính kèm (có thể null)

    //@NotBlank(message = "User ID không được để trống")
    String userId; // ID của người đăng bài
}

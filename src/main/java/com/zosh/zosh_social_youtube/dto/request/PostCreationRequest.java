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

    public @Size(max = 500, message = "CAPTION_INVALID") String getCaption() {
        return caption;
    }

    public void setCaption(@Size(max = 500, message = "CAPTION_INVALID") String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

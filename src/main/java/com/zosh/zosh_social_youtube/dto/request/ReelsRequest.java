package com.zosh.zosh_social_youtube.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReelsRequest {

    @NotNull(message = "User ID không được để trống")
    String userId;

    @NotBlank(message = "Tiêu đề không được để trống")
    String title;

    @NotBlank(message = "URL video không được để trống")
    String video;

    public @NotNull(message = "User ID không được để trống") String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID không được để trống") String userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Tiêu đề không được để trống") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Tiêu đề không được để trống") String title) {
        this.title = title;
    }

    public @NotBlank(message = "URL video không được để trống") String getVideo() {
        return video;
    }

    public void setVideo(@NotBlank(message = "URL video không được để trống") String video) {
        this.video = video;
    }
}

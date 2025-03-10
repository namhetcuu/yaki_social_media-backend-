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


}

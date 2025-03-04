package com.zosh.zosh_social_youtube.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowingResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
}

package com.zosh.zosh_social_youtube.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zosh.zosh_social_youtube.entity.UserFollower;
import com.zosh.zosh_social_youtube.entity.UserFollowing;
import com.zosh.zosh_social_youtube.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    String email;

    @JsonProperty("gender") // Đảm bảo tên field đúng chuẩn JSON nếu cần
    Gender gender;

    Set<String> roles;
    Set<String> followers;  // Danh sách user khác đang theo dõi user này
    Set<String> following;  // Danh sách mà user này đang theo dõi các user khác



}

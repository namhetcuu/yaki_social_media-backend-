package com.zosh.zosh_social_youtube.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("gender") // Đảm bảo tên field đúng chuẩn JSON nếu cần
    Gender gender;

    Set<RoleResponse> roles;
    
}

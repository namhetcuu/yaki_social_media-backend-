package com.zosh.zosh_social_youtube.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3,message = "USERNAME_INVALID")
    String username;
    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;
     // ✅ Map chính xác tên từ JSON
    String firstName;
     // ✅ Map chính xác tên từ JSON
    String lastName;
//    @Email(message = "EMAIL_INVALID")  // ✅ Đảm bảo email đúng format
    String email;  // ✅ Thêm email vào đây
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Đảm bảo format đúng
    LocalDate dob;


}

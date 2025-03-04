package com.zosh.zosh_social_youtube.dto.request;

import com.zosh.zosh_social_youtube.enums.EnumRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions; // ✅ Đảm bảo thuộc tính này tồn tại
}

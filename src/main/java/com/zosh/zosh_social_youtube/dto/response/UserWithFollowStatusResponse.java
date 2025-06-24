package com.zosh.zosh_social_youtube.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithFollowStatusResponse {
    private String id;
    private String username;
    private String avatar;
    private boolean isFollowed; // currentUser đã follow người này chưa
}

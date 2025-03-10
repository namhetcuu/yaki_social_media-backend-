package com.zosh.zosh_social_youtube.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_followers")
//Class này lưu trữ danh sách người đang theo dõi một người dùng.
public class UserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Người đang được follow (User này có nhiều followers)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id", nullable = false)
    User followingUser;

    // Người follow (User này follow nhiều người)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    User follower;

    LocalDateTime followedAt; // Thời gian follow

    @PrePersist
    protected void onCreate() {
        this.followedAt = LocalDateTime.now();
    }
}

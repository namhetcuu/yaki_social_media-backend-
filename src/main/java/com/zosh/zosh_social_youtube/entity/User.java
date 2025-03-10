package com.zosh.zosh_social_youtube.entity;

import com.zosh.zosh_social_youtube.enums.EnumRole;
import com.zosh.zosh_social_youtube.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    String email;

    @Enumerated(EnumType.STRING) // Đảm bảo lưu dưới dạng Enum thay vì String
    Gender gender;

    Set<String> roles;

    // Danh sách những người mình follow
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<UserFollowing> following = new HashSet<>();

    // Danh sách những người follow mình
    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<UserFollower> followers = new HashSet<>();;

    // Danh sách các bài viết đã lưu
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<SavedPost> savedPosts = new HashSet<>();

    // 💡 Thêm quan hệ với Reels
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Reels> reels;

    public Set<UserFollower> getFollowers() {
        return followers;
    }

    public Set<UserFollowing> getFollowing() {
        return following;
    }
}

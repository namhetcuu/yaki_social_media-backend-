package com.zosh.zosh_social_youtube.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Dùng UUID để tránh trùng lặp ID
    String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @ManyToOne(fetch = FetchType.LAZY) // Tránh load dữ liệu thừa
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> likedUsers = new HashSet<>();

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ✅ Getter để MapStruct có thể lấy userId
    public String getUserId() {
        return user != null ? user.getId() : null;
    }

    // ✅ Getter để MapStruct có thể lấy postId
    public String getPostId() {
        return post != null ? post.getId() : null;
    }

    // ✅ Getter để MapStruct lấy username
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    // ✅ Logic thích / bỏ thích bình luận
    public void likeComment(User user) {
        likedUsers.add(user);
    }

    public void unlikeComment(User user) {
        likedUsers.remove(user);
    }
    public String getId() {
        return id;
    }
    public User getUser() { return user; }
    public Post getPost() { return post; }
}

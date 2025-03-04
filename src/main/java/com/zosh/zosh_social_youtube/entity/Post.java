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
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "TEXT") // Nếu dùng MySQL, PostgreSQL
    private String caption;

    @Column(length = 500)
    String imageUrl;

    @Column(length = 500)
    String videoUrl;

    //Chỉ ra rằng một bài viết (Post) thuộc về một người dùng duy nhất (User).
    //Chỉ tải dữ liệu User khi cần thiết (Lazy Loading).
    //Tránh việc tải cả thông tin User khi chỉ cần lấy bài viết, giúp tối ưu hiệu suất.
    //Nếu đặt FetchType.EAGER, khi lấy một Post, Hibernate sẽ tự động tải
    // toàn bộ dữ liệu của User liên quan đến bài viết đó → có thể gây chậm.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @Column(nullable = false)
    int likeCount;

    //Một bài viết (Post) có thể được nhiều người dùng (User) thích
    //Một người dùng (User) có thể thích nhiều bài viết (Post) khác nhau.
    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
            //Không cho phép trùng lặp, mỗi người chỉ có thể thích một lần(dùng Set)
    Set<User> likedUsers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        likeCount = 0; // Mặc định số lượt thích là 0 khi bài viết mới tạo
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public User getUser() {
        return user;
    }
    public Set<User> getLikedUsers() {
        return likedUsers;
    }
    public String getImageUrl() {
        return imageUrl;
    }

}

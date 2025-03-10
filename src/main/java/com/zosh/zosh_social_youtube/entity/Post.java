package com.zosh.zosh_social_youtube.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "TEXT") // Nếu dùng MySQL, PostgreSQL
    String caption;

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

    // Thêm danh sách comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Comment> comments = new ArrayList<>();

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

    public List<Comment> getComments() {
        return comments == null ? new ArrayList<>() : comments;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setLikedUsers(Set<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

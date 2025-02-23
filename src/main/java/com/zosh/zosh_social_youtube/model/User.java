package com.zosh.zosh_social_youtube.model;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;
    String username;
    String password;
//    @Column(name = "first_name", nullable = false, length = 100)
    String firstName;
//    @Column(name = "last_name", nullable = false, length = 100)
    String lastName;

//    @Column(unique = true, length = 100)
//@Column(name = "email", nullable = false, unique = true)
    String email;
//    @Column
//    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    //Khi dùng @ElementCollection, Hibernate
    // sẽ tự động tạo bảng riêng để lưu danh sách các phần tử của tập hợp (Set hoặc List).
    //Các bảng này không có primary key riêng, chỉ có khóa ngoại tham chiếu đến bảng chính (user).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    Set<String> roles = new HashSet<>();

    // Danh sách followers: Những người đang follow user này
    @ElementCollection
    @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_id")
    List<Integer> followers = new ArrayList<>();

    // Danh sách following: Những người mà user này đang follow
    @ElementCollection
    @CollectionTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following_id")
    List<String> following = new ArrayList<>();

    // Danh sách bài viết đã lưu của user này
    @ManyToMany
    @JoinTable(
            name = "saved_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    Set<Post> savedPosts = new HashSet<>();

    // Quan hệ 1-N: Một user có thể có nhiều bài viết
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post> posts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (roles == null) roles = new HashSet<>();
        if (followers == null) followers = new ArrayList<>();
        if (following == null) following = new ArrayList<>();
        if (savedPosts == null) savedPosts = new HashSet<>();
    }
}

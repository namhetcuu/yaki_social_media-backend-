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

    // 💡 Danh sách các reels do người dùng tạo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Reels> reels;

    // Danh sách các cuộc trò chuyện mà user tham gia
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    List<Chat> chats = new ArrayList<>();

    // 💡 Thêm quan hệ với Message (User là người gửi tin nhắn)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Message> messages = new ArrayList<>();

    public Set<UserFollower> getFollowers() {
        return followers;
    }

    public Set<UserFollowing> getFollowing() {
        return following;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setFollowing(Set<UserFollowing> following) {
        this.following = following;
    }

    public void setFollowers(Set<UserFollower> followers) {
        this.followers = followers;
    }

    public Set<SavedPost> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(Set<SavedPost> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public List<Reels> getReels() {
        return reels;
    }

    public void setReels(List<Reels> reels) {
        this.reels = reels;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

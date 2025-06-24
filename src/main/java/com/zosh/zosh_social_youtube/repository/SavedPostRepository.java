package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.SavedPost;
import com.zosh.zosh_social_youtube.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    void deleteByPostId(String postId); // Thêm phương thức xóa theo `postId`
    Optional<SavedPost> findByUserAndPost(User user, Post post);
    boolean existsByUserIdAndPostId(String userId, String postId);
}

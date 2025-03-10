package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // ✅ Đúng cú pháp Spring Data JPA
    List<Comment> findByPost_Id(String postId);
}

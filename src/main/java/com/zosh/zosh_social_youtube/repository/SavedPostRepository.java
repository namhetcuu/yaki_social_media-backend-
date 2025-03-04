package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, String> {
    void deleteByPostId(String postId); // Thêm phương thức xóa theo `postId`
}

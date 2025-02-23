package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.model.Post;
import com.zosh.zosh_social_youtube.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    //tìm kiếm theo user
    List<Post> findByUser(User user);
    //tìm bài viết theo từ khoa trong caption
    List<Post> findByCaptionContainingIgnoreCase(String keyword);
    //tìm 10 bài viết mới nhất
    List<Post> findTop10ByOrderByCreatedAtDesc();
    //Lấy 10 bài viết có nhiều lượt thích nhất.
    List<Post> findTop10ByOrderByLikeCountDesc();
    //Xóa tất cả bài viết khi người dùng bị xóa.
    void deleteByUser(User user);

}

package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    // ✅ Lấy danh sách bài viết của một user kèm theo comments và người dùng bình luận
    @EntityGraph(attributePaths = {"user", "comments", "comments.user", "likedUsers"})
    List<Post> findByUser(User user);

    // ✅ Tìm bài viết theo từ khóa trong caption (Không phân biệt hoa thường)
    @EntityGraph(attributePaths = {"user", "comments", "comments.user", "likedUsers"})
    List<Post> findByCaptionContainingIgnoreCase(String keyword);

    // ✅ Lấy 10 bài viết mới nhất kèm theo thông tin comments và likes
    @EntityGraph(attributePaths = {"user", "comments", "comments.user", "likedUsers"})
    List<Post> findTop10ByOrderByCreatedAtDesc();

    // ✅ Lấy 10 bài viết có nhiều lượt thích nhất
    @EntityGraph(attributePaths = {"user", "comments", "comments.user", "likedUsers"})
    List<Post> findTop10ByOrderByLikeCountDesc();

    // ✅ Xóa tất cả bài viết khi người dùng bị xóa
    void deleteByUser(User user);

    // ✅ Truy vấn tất cả bài viết và lấy luôn comments + likes để tránh N+1 Query Issue
    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.likedUsers " +
            "LEFT JOIN FETCH p.comments c " +
            "LEFT JOIN FETCH c.user")
    List<Post> findAllPosts();
}

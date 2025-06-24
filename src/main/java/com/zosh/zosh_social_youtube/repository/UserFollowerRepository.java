package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.entity.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    boolean existsByFollowerAndFollowingUser(User follower, User followingUser);

    // Lấy danh sách ID của followers của user (Những người theo dõi user này)
    @Query("SELECT uf.follower.id FROM UserFollower uf WHERE uf.followingUser.id = :userId")
    Set<String> findFollowerIdsByUserId(@Param("userId") String userId);

    // Lấy danh sách ID của following của user (User này đang theo dõi ai)
    @Query("SELECT uf.followingUser.id FROM UserFollower uf WHERE uf.follower.id = :userId")
    Set<String> findFollowingIdsByUserId(@Param("userId") String userId);

    Optional<UserFollower> findByFollowerAndFollowingUser(User follower, User followingUser);

}

package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.entity.UserFollowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {
    boolean existsByFollowerAndFollowingUser(User follower, User followingUser);
}

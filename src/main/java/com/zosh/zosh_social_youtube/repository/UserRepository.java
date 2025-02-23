package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    //Kiểm tra xem email đã tồn tại trong hệ thống chưa
    boolean existsByEmail(String email);
    //Tìm kiếm người dùng theo email.
    Optional<User> findByEmail(String email);
    //Lọc danh sách người dùng theo vai trò (Admin, User, v.v.).
    List<User> findByRolesContaining(String role);
}

package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    // Tìm quyền theo tên
    Optional<Permission> findByName(String name);
}

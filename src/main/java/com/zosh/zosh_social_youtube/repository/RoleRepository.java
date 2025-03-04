package com.zosh.zosh_social_youtube.repository;

import com.zosh.zosh_social_youtube.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
}

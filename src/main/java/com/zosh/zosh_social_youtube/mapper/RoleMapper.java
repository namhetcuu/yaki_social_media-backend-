package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.RoleRequest;
import com.zosh.zosh_social_youtube.dto.response.RoleResponse;
import com.zosh.zosh_social_youtube.dto.response.PermissionResponse;
import com.zosh.zosh_social_youtube.entity.Permission;
import com.zosh.zosh_social_youtube.entity.Role;
import com.zosh.zosh_social_youtube.repository.PermissionRepository;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}

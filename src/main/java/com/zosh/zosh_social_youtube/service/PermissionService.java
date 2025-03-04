package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.dto.request.PermissionRequest;
import com.zosh.zosh_social_youtube.dto.response.PermissionResponse;
import com.zosh.zosh_social_youtube.entity.Permission;
import com.zosh.zosh_social_youtube.mapper.PermissionMapper;
import com.zosh.zosh_social_youtube.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional // Đảm bảo tất cả thao tác DB được thực hiện an toàn
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}

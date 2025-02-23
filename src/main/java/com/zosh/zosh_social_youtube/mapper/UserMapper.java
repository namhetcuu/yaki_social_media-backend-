package com.zosh.zosh_social_youtube.mapper;


import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true) // ID do Hibernate tạo
    @Mapping(target = "roles", ignore = true) // Roles sẽ được set thủ công
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", source = "roles") // Đảm bảo roles được map
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}


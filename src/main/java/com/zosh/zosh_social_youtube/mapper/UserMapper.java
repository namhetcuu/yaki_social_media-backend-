package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.FollowerResponse;
import com.zosh.zosh_social_youtube.dto.response.FollowingResponse;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.entity.Role;
import com.zosh.zosh_social_youtube.entity.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {


    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}

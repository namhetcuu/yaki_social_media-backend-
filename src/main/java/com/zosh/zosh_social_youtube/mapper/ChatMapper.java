package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.ChatRequest;
import com.zosh.zosh_social_youtube.dto.response.ChatResponse;
import com.zosh.zosh_social_youtube.entity.Chat;
import com.zosh.zosh_social_youtube.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(source = "users", target = "userIds", qualifiedByName = "mapUsersToUserIds")
    ChatResponse toChatResponse(Chat chat);

    @Mapping(source = "userIds", target = "users", qualifiedByName = "mapUserIdsToUsers")
    Chat toChatEntity(ChatRequest chatRequest);

    @Named("mapUsersToUserIds")
    static List<String> mapUsersToUserIds(List<User> users) {
        return users != null ? users.stream().map(User::getId).collect(Collectors.toList()) : null;
    }

    @Named("mapUserIdsToUsers")
    static List<User> mapUserIdsToUsers(List<String> userIds) {
        return userIds != null ? userIds.stream().map(id -> {
            User user = new User();
            user.setId(id);
            return user;
        }).collect(Collectors.toList()) : null;
    }
}

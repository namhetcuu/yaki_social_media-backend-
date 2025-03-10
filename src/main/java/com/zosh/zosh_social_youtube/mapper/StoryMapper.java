package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.dto.response.StoryResponse;
import com.zosh.zosh_social_youtube.entity.Comment;
import com.zosh.zosh_social_youtube.entity.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(source = "user.id", target = "userId") // Map user.id -> userId
    StoryResponse toStoryResponse(Story story);

}

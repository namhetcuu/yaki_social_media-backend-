package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.response.ReelsResponse;
import com.zosh.zosh_social_youtube.entity.Reels;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReelsMapper {


    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "video", target = "video")
    })
    ReelsResponse toReelsResponse(Reels reels);
}

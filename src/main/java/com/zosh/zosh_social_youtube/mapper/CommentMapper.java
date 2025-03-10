package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    //CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    //@Mapping(target = "userId", expression = "java(comment.getUser() != null ? comment.getUser().getId() : null)")
    //@Mapping(target = "username", expression = "java(comment.getUser() != null ? comment.getUser().getUsername() : null)")
    //@Mapping(target = "postId", expression = "java(comment.getPost() != null ? comment.getPost().getId() : null)")
    @Mapping(target = "userId", expression = "java(comment.getUser() != null ? comment.getUser().getId() : null)")
    @Mapping(target = "postId", expression = "java(comment.getPost() != null ? comment.getPost().getId() : null)")
    CommentResponse toCommentResponse(Comment comment);
}

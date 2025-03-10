package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.PostCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.PostUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.dto.response.PostResponse;
import com.zosh.zosh_social_youtube.entity.Comment;
import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "user", ignore = true)
    //@Mapping(target = "authorUsername", ignore = true)
    Post toPost(PostCreationRequest request);

//    @Mapping(target = "authorId", expression = "java(post.getUser() != null ? post.getUser().getId() : null)")
//    @Mapping(target = "authorUsername", expression = "java(post.getUser() != null ? post.getUser().getUsername() : null)")
//    @Mapping(target = "likedUsers", expression = "java(post.getLikedUsers() != null ? post.getLikedUsers().stream().map(User::getId).collect(Collectors.toSet()) : new HashSet<>())")
//    @Mapping(target = "imageUrl", expression = "java(post.getImageUrl())")
    @Mapping(target = "id", source = "id")  // ✅ Bổ sung ánh xạ ID
    @Mapping(target = "caption", source = "caption")  // ✅ Bổ sung caption
    @Mapping(target = "createdAt", source = "createdAt")  // ✅ Bổ sung createdAt
    @Mapping(target = "updatedAt", source = "updatedAt")  // ✅ Bổ sung updatedAt
    @Mapping(target = "likeCount", source = "likeCount")  // ✅ Bổ sung likeCount
    @Mapping(target = "authorId", expression = "java(post.getUser() != null ? post.getUser().getId() : null)")
    @Mapping(target = "authorUsername", expression = "java(post.getUser() != null ? post.getUser().getUsername() : null)")
    @Mapping(target = "likedUsers", qualifiedByName = "mapLikedUsers")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "comments", qualifiedByName = "mapComments")  // ✅ Thêm ánh xạ comments
    PostResponse toPostResponse(Post post);

    void updatePost(@MappingTarget Post post, PostUpdateRequest request);

    @Named("mapLikedUsers")
    default Set<String> mapLikedUsers(Set<User> likedUsers) {
        return likedUsers == null ? Set.of() :
                likedUsers.stream().map(User::getId).collect(Collectors.toSet());
    }

    @Named("mapComments")
    default List<CommentResponse> mapComments(List<Comment> comments) {
        if (comments == null) {
            return List.of();
        }
        return comments.stream().map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt())
                .build()).collect(Collectors.toList());
    }
}

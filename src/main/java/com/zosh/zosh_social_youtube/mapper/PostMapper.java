package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.PostCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.PostUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.PostResponse;
import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.User;
import org.mapstruct.*;

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
    @Mapping(target = "authorId", expression = "java(post.getUser() != null ? post.getUser().getId() : null)")
    @Mapping(target = "authorUsername", expression = "java(post.getUser() != null ? post.getUser().getUsername() : null)")
    @Mapping(target = "likedUsers", qualifiedByName = "mapLikedUsers")
    @Mapping(target = "imageUrl", source = "imageUrl")

    PostResponse toPostResponse(Post post);

    void updatePost(@MappingTarget Post post, PostUpdateRequest request);

    @Named("mapLikedUsers")
    default Set<String> mapLikedUsers(Set<User> likedUsers) {
        return likedUsers == null ? Set.of() :
                likedUsers.stream().map(User::getId).collect(Collectors.toSet());
    }
}

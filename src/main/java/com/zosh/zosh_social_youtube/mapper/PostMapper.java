package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.PostCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.PostUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.dto.response.PostResponse;
import com.zosh.zosh_social_youtube.entity.Comment;
import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.repository.SavedPostRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    protected SavedPostRepository savedPostRepository;

    @Mapping(target = "user", ignore = true)
    public abstract Post toPost(PostCreationRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "caption", source = "caption")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "authorId", expression = "java(post.getUser() != null ? post.getUser().getId() : null)")
    @Mapping(target = "authorUsername", expression = "java(post.getUser() != null ? post.getUser().getUsername() : null)")
    @Mapping(target = "likedUsers", qualifiedByName = "mapLikedUsers")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "comments", qualifiedByName = "mapComments")
    @Mapping(target = "bookmarked", constant = "false")
    public abstract PostResponse toPostResponse(Post post);

    public PostResponse toPostResponse(Post post, User user) {
        boolean isBookmarked = savedPostRepository.existsByUserIdAndPostId(user.getId(), post.getId());

        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeCount(post.getLikeCount())
                .authorId(post.getUser() != null ? post.getUser().getId() : null)
                .authorUsername(post.getUser() != null ? post.getUser().getUsername() : null)
                .likedUsers(mapLikedUsers(post.getLikedUsers()))
                .imageUrl(post.getImageUrl())
                .comments(mapComments(post.getComments()))
                .bookmarked(isBookmarked)
                .build();
    }

    public abstract void updatePost(@MappingTarget Post post, PostUpdateRequest request);

    @Named("mapLikedUsers")
    protected Set<String> mapLikedUsers(Set<User> likedUsers) {
        return likedUsers == null ? Set.of() :
                likedUsers.stream().map(User::getId).collect(Collectors.toSet());
    }

    @Named("mapComments")
    protected List<CommentResponse> mapComments(List<Comment> comments) {
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
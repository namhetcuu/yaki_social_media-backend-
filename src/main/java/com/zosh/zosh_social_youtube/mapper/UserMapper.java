package com.zosh.zosh_social_youtube.mapper;

import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.entity.UserFollower;
import com.zosh.zosh_social_youtube.entity.UserFollowing;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    User toUser(UserCreationRequest request);
//
//    @Mapping(target = "followers", expression = "java(mapFollowersToIds(user.getFollowers()))")
//    @Mapping(target = "following", expression = "java(mapFollowingsToIds(user.getFollowing()))")
//    UserResponse toUserResponse(User user);
//
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
//
//    default Set<String> mapFollowersToIds(Set<UserFollower> followers) {
//        if (followers == null) return Set.of();
//        return followers.stream()
//                .map(follower -> follower.getFollower().getId()) // Lấy ID của người follow mình
//                .collect(Collectors.toSet());
//    }
//
//    default Set<String> mapFollowingsToIds(Set<UserFollowing> following) { // Sửa `followings` thành `following`
//        if (following == null) return Set.of();
//        return following.stream()
//                .map(followingUser -> followingUser.getFollowingUser().getId()) // Lấy ID của người mình follow
//                .collect(Collectors.toSet());
//    }
//}
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    User toUser(UserCreationRequest request);
//
//    @Mapping(target = "followers", expression = "java(mapFollowersToIds(user.getFollowers()))")
//    @Mapping(target = "following", expression = "java(mapFollowingsToIds(user.getFollowing()))")
//    UserResponse toUserResponse(User user);
//
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
//
//    @Named("mapFollowersToIds")
//    default Set<String> mapFollowersToIds(Set<UserFollower> followers) {
//        if (followers == null) return Set.of();
//        return followers.stream()
//                .map(follower -> follower.getFollower().getId()) // Lấy ID của người follow mình
//                .collect(Collectors.toSet());
//    }
//
//    @Named("mapFollowingsToIds")
//    default Set<String> mapFollowingsToIds(Set<UserFollowing> following) {
//        if (following == null) return Set.of();
//        return following.stream()
//                .map(followingUser -> followingUser.getFollowingUser().getId()) // Lấy ID của người mình follow
//                .collect(Collectors.toSet());
//    }
//}
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    //@Mapping(target = "followers", expression = "java(mapFollowersToIds(user.getFollowers()))")
    //@Mapping(target = "following", expression = "java(mapFollowingsToIds(user.getFollowing()))")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "followers", ignore = true) // If followers need special handling
    @Mapping(target = "following", ignore = true) // If following needs special handling
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default Set<String> mapFollowersToIds(Set<UserFollower> followers) {
        if (followers == null) return Set.of();
        return followers.stream()
                .map(follower -> follower.getFollower().getId())
                .collect(Collectors.toSet());
    }

    default Set<String> mapFollowingsToIds(Set<UserFollowing> following) {
        if (following == null) return Set.of();
        return following.stream()
                .map(followingUser -> followingUser.getFollowingUser().getId())
                .collect(Collectors.toSet());
    }
}




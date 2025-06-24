package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.constant.PredefinedRole;
import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.dto.response.UserWithFollowStatusResponse;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.entity.UserFollower;
import com.zosh.zosh_social_youtube.enums.EnumRole;
import com.zosh.zosh_social_youtube.exception.AppException;
import com.zosh.zosh_social_youtube.exception.ErrorCode;
import com.zosh.zosh_social_youtube.mapper.UserMapper;
import com.zosh.zosh_social_youtube.repository.UserFollowerRepository;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    private final UserFollowerRepository userFollowerRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        log.info("Creating new user with username: {}", request.getUsername());

        // Chuyển đổi từ DTO sang Entity
        User user = userMapper.toUser(request);
        log.info("Mapped User entity: {}", user);

        if (user == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(EnumRole.USER.name());

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }



    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        log.info("Authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No authenticated user found");
            throw new AppException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        String username = authentication.getName();
        log.info("Fetching info for logged-in user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        log.info("Fetched User: {}", user);  // 🔹 Check if user data is correct

        if (user == null) {
            log.error("User not found in database for username: {}", username);
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Debugging user fields before mapping
        log.info("User details - ID: {}, Username: {}, Email: {}, Roles: {}",
                user.getId(), user.getUsername(), user.getEmail(), user.getRoles());

        UserResponse userResponse = userMapper.toUserResponse(user);

        if (userResponse == null) {
            log.error("UserResponse mapping failed for user: {}", username);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        log.info("Mapped UserResponse: {}", userResponse); // 🔹 Check mapped response

        return userResponse;
    }



    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        // Kiểm tra nếu password không null thì mới mã hóa và cập nhật
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Cập nhật giới tính nếu có
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUser(String id) {
        log.info("Fetching user with ID: {}", id);

        // Fetch user from database
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", id);
                    return new AppException(ErrorCode.USER_NOT_EXISTED);
                });

        // Fetch followers & following
        Set<String> followers = userFollowerRepository.findFollowerIdsByUserId(id);
        Set<String> following = userFollowerRepository.findFollowingIdsByUserId(id);

        // Ensure user details are mapped correctly
        UserResponse userResponse = userMapper.toUserResponse(user);
        if (userResponse == null) {
            log.error("User mapping failed for user ID: {}", id);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        userResponse.setFollowers(followers != null ? followers : new HashSet<>());
        userResponse.setFollowing(following != null ? following : new HashSet<>());

        log.info("User {} fetched successfully", id);
        return userResponse;
    }


    //user1 (người follow) và user2 (người được follow)
    public UserResponse followUser(String user1Id, String user2Id) {
        log.info("User {} is following user {}", user1Id, user2Id);

        // 1. Tìm user1 (người follow) và user2 (người được follow)
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (userFollowerRepository.existsByFollowerAndFollowingUser(user1, user2)) {
            throw new AppException(ErrorCode.ALREADY_FOLLOWING);
        }

        UserFollower followRelation = UserFollower.builder()
                .follower(user1)
                .followingUser(user2)
                .build();

        userFollowerRepository.save(followRelation);
        return userMapper.toUserResponse(user1);
    }

    public void unfollowUser(String user1Id, String user2Id) {
        log.info("User {} is unfollowing user {}", user1Id, user2Id);

        // 1. Tìm user1 và user2
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Kiểm tra xem đã follow chưa
        UserFollower existingFollow = userFollowerRepository
                .findByFollowerAndFollowingUser(user1, user2)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOLLOWING));

        // 3. Xoá quan hệ follow
        userFollowerRepository.delete(existingFollow);
    }

    public List<UserWithFollowStatusResponse> getUsersWithFollowStatus(String currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<User> allUsers = userRepository.findAll();

        Set<String> followingIds = userFollowerRepository.findFollowingIdsByUserId(currentUserId);

        return allUsers.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // loại chính mình
                .map(user -> UserWithFollowStatusResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .avatar(user.getProfilePicture())
                        .isFollowed(followingIds.contains(user.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }


    public void sendPasswordResetEmail(User user){

    }

    public void updatePassword(User user, String newPassword) {

    }

    public List<UserResponse> searchUsers(String query) {
        log.info("Searching users with query: {}", query);

        // Tìm kiếm user theo username hoặc email chứa query (không phân biệt hoa thường)
        List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);

        // Chuyển đổi danh sách User thành UserResponse
        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .toList();

        log.info("Found {} users matching query '{}'", userResponses.size(), query);

        return userResponses;
    }


//    public UserResponse findUserByEmail(String email) {
//
//    }

//    public UserResponse updateUserDetails()
}

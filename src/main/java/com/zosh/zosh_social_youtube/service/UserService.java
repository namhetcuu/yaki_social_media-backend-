package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.constant.PredefinedRole;
import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
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
import java.util.Set;

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
        String username = context.getAuthentication().getName();

        log.info("Fetching info for logged-in user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
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

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        log.info("Fetching user with ID: {}", id);

        // Tìm user trong database
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách followers & following
        Set<String> followers = userFollowerRepository.findFollowerIdsByUserId(id);
        Set<String> following = userFollowerRepository.findFollowingIdsByUserId(id);

        // Map user thành UserResponse
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFollowers(followers);
        userResponse.setFollowing(following);

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

        // 2. Kiểm tra nếu đã follow trước đó
        if (userFollowerRepository.existsByFollowerAndFollowingUser(user1, user2)) {
            throw new AppException(ErrorCode.ALREADY_FOLLOWING);
        }

        // 3. Tạo mới UserFollower
        UserFollower followRelation = UserFollower.builder()
                .follower(user1)
                .followingUser(user2)
                .build();

        // 4. Lưu vào database
        userFollowerRepository.save(followRelation);

        // 5. Trả về thông tin user sau khi follow
        return userMapper.toUserResponse(user1);
    }


    public void sendPasswordResetEmail(User user){

    }

    public void updatePassword(User user, String newPassword) {

    }

//    public UserResponse findUserByEmail(String email) {
//
//    }

//    public UserResponse updateUserDetails()
}

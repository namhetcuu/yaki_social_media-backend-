package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.UserUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        log.info("Fetching all users");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        log.info("Fetching user with ID: {}", userId);

        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Received UserCreationRequest: {}", request);
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        log.info("Updating user with ID: {}", userId);
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getMyInfo() {
        log.info("Fetching info for the current user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/follow/{userId1}/{userId2}")
    public ApiResponse<UserResponse> followUser(@PathVariable String userId1, @PathVariable String userId2) {
        UserResponse userResponse = userService.followUser(userId1, userId2);

        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
}

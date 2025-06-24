package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.PostCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.PostUpdateRequest;
import com.zosh.zosh_social_youtube.dto.request.UserCreationRequest;
import com.zosh.zosh_social_youtube.dto.response.PostResponse;
import com.zosh.zosh_social_youtube.dto.response.UserResponse;
import com.zosh.zosh_social_youtube.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;
    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {

        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPosts())
                .build();
    }
    @PostMapping("/users/{userId}")
    @PreAuthorize("isAuthenticated()")
//    ✅ Lấy userId từ JWT (@AuthenticationPrincipal UserDetails):
//    @AuthenticationPrincipal lấy thông tin người dùng từ CustomUserDetails
//    Đảm bảo chỉ người dùng hợp lệ mới có thể tạo bài viết.
//    Tránh giả mạo userId.
    ApiResponse<PostResponse> createPost(@RequestBody @Valid PostCreationRequest request,
                                         @PathVariable String userId) {

        //String userId = userDetails.getUsername();
        request.setUserId(userId);
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }
    @DeleteMapping("/{postId}/users/{userId}")
    ApiResponse<String> deletePostByUserId(@PathVariable String postId, @PathVariable String userId){
        postService.deletePost(postId, userId);
        return ApiResponse.<String>builder()
                .result("Post has been deleted")
                .build();
    }
    // Lấy bài viết theo ID
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPost(postId))
                .build();
    }
    //lấy post theo userId
    @GetMapping("/users/{userId}")
    public ApiResponse<List<PostResponse>> getPostByUserId(@PathVariable String userId) {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostByUserId(userId))
                .build();
    }
    // Chỉnh sửa bài viết
    @PutMapping("/{postId}/users/{userId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable String postId,
                                                @PathVariable String userId,
                                                @RequestBody @Valid PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, userId, request))
                .build();
    }
    // Like/Unlike bài viết dưa theo id của post và id của user
    @PutMapping("/like/{postId}/users/{userId}")
    public ApiResponse<PostResponse> likePost(@PathVariable String postId, @PathVariable String userId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.likePost(postId, userId))
                .build();
    }
    //lưu bài vết theo id user
    @PutMapping("/save/{postId}/users/{userId}")
    public ApiResponse<PostResponse> savedPostHandler(@PathVariable String postId,
                                                      @PathVariable String userId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.savedPost(postId, userId))
                .build();
    }

    @DeleteMapping("/unsave/{postId}/users/{userId}")
    public ApiResponse<PostResponse> unsavePost(@PathVariable String postId, @PathVariable String userId) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.unsavePost(postId,userId))
                .build();
    }

}

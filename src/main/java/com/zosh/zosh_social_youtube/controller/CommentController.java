package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.configuration.UserPrincipal;
import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.CommentRequest;
import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class CommentController {

    final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @PutMapping("/like/{commentId}")
    public ApiResponse<CommentResponse> likeComment(@PathVariable Long commentId,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.likeComment(commentId, userPrincipal.getId()))
                .build();
    }


}

package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.CommentRequest;
import com.zosh.zosh_social_youtube.dto.request.ReelsRequest;
import com.zosh.zosh_social_youtube.dto.response.CommentResponse;
import com.zosh.zosh_social_youtube.dto.response.ReelsResponse;
import com.zosh.zosh_social_youtube.service.ReelsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reels")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ReelsController {

    ReelsService reelsService;

    /**
     * API tạo Reels mới
     */
    @PostMapping
    public ApiResponse<ReelsResponse> createReel(@RequestBody ReelsRequest request) {
        return ApiResponse.<ReelsResponse>builder()
                .result(reelsService.createReel(request))
                .build();
    }


    /**
     * API lấy danh sách tất cả Reels
     */
    @GetMapping
    public ApiResponse<List<ReelsResponse>> findAllReels() {
        return ApiResponse.<List<ReelsResponse>>builder()
                .result(reelsService.findAllReels())
                .build();
    }

    /**
     * API lấy danh sách Reels của một User
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReelsResponse>> findUsersReels(@PathVariable String userId) {
        return ApiResponse.<List<ReelsResponse>>builder()
                .result(reelsService.findUsersReel(userId))
                .build();
    }

}

package com.zosh.zosh_social_youtube.controller;

import com.zosh.zosh_social_youtube.dto.request.ApiResponse;
import com.zosh.zosh_social_youtube.dto.request.StoryRequest;
import com.zosh.zosh_social_youtube.dto.response.StoryResponse;
import com.zosh.zosh_social_youtube.service.StoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class StoryController {

    StoryService storyService;

    // API tạo Story mới
    @PostMapping
    public ApiResponse<StoryResponse> createStory(@RequestBody StoryRequest request) {
        return ApiResponse.<StoryResponse>builder()
                .result(storyService.createStory(request))
                .build();
    }

    // API lấy danh sách Story của một User
    @GetMapping("/user/{userId}")
    public ApiResponse<List<StoryResponse>> findStoryByUserId(@PathVariable String userId) {
        return ApiResponse.<List<StoryResponse>>builder()
                .result(storyService.findStoryByUserId(userId))
                .build();
    }
}

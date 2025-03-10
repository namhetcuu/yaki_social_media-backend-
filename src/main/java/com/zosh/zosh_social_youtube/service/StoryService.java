package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.dto.request.StoryRequest;
import com.zosh.zosh_social_youtube.dto.response.StoryResponse;
import com.zosh.zosh_social_youtube.entity.Story;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.exception.AppException;
import com.zosh.zosh_social_youtube.exception.ErrorCode;
import com.zosh.zosh_social_youtube.mapper.StoryMapper;
import com.zosh.zosh_social_youtube.repository.StoryRepository;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoryService {

    StoryRepository storyRepository;
    UserRepository userRepository;
    StoryMapper storyMapper;

    public StoryResponse createStory(StoryRequest request) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo story từ request
        Story story = Story.builder()
                .user(user)
                .image(request.getImage())
                .caption(request.getCaption())
                .build();

        // Lưu vào database
        Story savedStory = storyRepository.save(story);

        log.info("Story được tạo thành công với ID: {}", savedStory.getId());

        // Chuyển đổi entity sang DTO trước khi trả về
        return storyMapper.toStoryResponse(savedStory);
    }

    // Lấy danh sách Story của một User
    public List<StoryResponse> findStoryByUserId(String userId) {
        // Kiểm tra user có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        List<Story> stories = storyRepository.findByUserId(userId);
        log.info("Tìm thấy {} story của userId: {}", stories.size(), userId);

        // Chuyển đổi danh sách entity sang danh sách DTO
        return stories.stream()
                .map(storyMapper::toStoryResponse)
                .collect(Collectors.toList());
    }
}

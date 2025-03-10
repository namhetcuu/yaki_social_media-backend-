package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.dto.request.ReelsRequest;
import com.zosh.zosh_social_youtube.dto.response.ReelsResponse;
import com.zosh.zosh_social_youtube.entity.Reels;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.exception.AppException;
import com.zosh.zosh_social_youtube.exception.ErrorCode;
import com.zosh.zosh_social_youtube.mapper.ReelsMapper;
import com.zosh.zosh_social_youtube.repository.ReelsRepository;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReelsService {

    private final ReelsRepository reelsRepository;
    private final UserRepository userRepository;
    private final ReelsMapper reelsMapper;

    //Lưu Reel mới với thông tin User.
    @Transactional
    public ReelsResponse createReel(ReelsRequest request) {

        // Kiểm tra dữ liệu đầu vào
        if (request.getUserId() == null || request.getTitle() == null || request.getVideo() == null) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // Lấy user từ request
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Debug: Kiểm tra user có đúng không
        log.info("Found User: {}", user.getId());

        // Tạo Reel mới
        Reels reel = Reels.builder()
                .title(request.getTitle())
                .video(request.getVideo())
                .user(user)
                .build();

        // Lưu vào DB
        Reels savedReel = reelsRepository.save(reel);

        // Debug: Kiểm tra reel đã lưu
        log.info("Saved Reel ID: {}", savedReel.getId());

        // Trả về response
        return reelsMapper.toReelsResponse(savedReel);

    }

    // Lấy danh sách tất cả các Reels
    public List<ReelsResponse> findAllReels() {
        List<Reels> reels = reelsRepository.findAll();
        return reels.stream()
                .map(reelsMapper::toReelsResponse)
                .collect(Collectors.toList());
    }

    // Lấy danh sách Reels của một User
    public List<ReelsResponse> findUsersReel(String userId) {
        // Kiểm tra xem User có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        List<Reels> userReels = reelsRepository.findByUserId(userId);
        return userReels.stream()
                .map(reelsMapper::toReelsResponse)
                .collect(Collectors.toList());
    }

}

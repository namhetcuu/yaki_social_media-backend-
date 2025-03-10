package com.zosh.zosh_social_youtube.service;

import com.zosh.zosh_social_youtube.dto.request.PostCreationRequest;
import com.zosh.zosh_social_youtube.dto.request.PostUpdateRequest;
import com.zosh.zosh_social_youtube.dto.response.PostResponse;
import com.zosh.zosh_social_youtube.exception.AppException;
import com.zosh.zosh_social_youtube.exception.ErrorCode;
import com.zosh.zosh_social_youtube.mapper.PostMapper;
import com.zosh.zosh_social_youtube.entity.Post;
import com.zosh.zosh_social_youtube.entity.SavedPost;
import com.zosh.zosh_social_youtube.entity.User;
import com.zosh.zosh_social_youtube.repository.PostRepository;
import com.zosh.zosh_social_youtube.repository.SavedPostRepository;
import com.zosh.zosh_social_youtube.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    SavedPostRepository savedPostRepository;
    PostMapper postMapper;

    /**
     * Tạo bài viết mới
     */
    public PostResponse createPost(PostCreationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Chuyển đổi từ request sang Post entity
        Post post = postMapper.toPost(request);
        System.out.println("Image URL: " + request.getImageUrl());
        post.setUser(user); // Liên kết bài viết với người dùng

        return postMapper.toPostResponse(postRepository.save(post));
    }

    /**
     * Xóa bài viết theo ID
     */
    @Transactional
    public String deletePost(String postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // Xóa các bản ghi liên quan trong bảng saved_posts trước khi xóa bài viết
        savedPostRepository.deleteByPostId(postId);

        // Xóa bài viết
        postRepository.delete(post);
        return "Post deleted successfully";
    }



    /**
     * Lấy tất cả bài viết
     */
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy bài viết theo ID
     */
    public PostResponse getPost(String postId) {
        return postRepository.findById(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * Lấy danh sách bài viết theo userId
     */
    public List<PostResponse> getPostByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật nội dung bài viết
     */
    @Transactional
    public PostResponse updatePost(String postId,String userId, PostUpdateRequest request) {
        Post post = postRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (request.getCaption() != null) {
            post.setCaption(request.getCaption());
        }
        if (request.getImageUrl() != null) {
            post.setImageUrl(request.getImageUrl());
        }
        if (request.getVideoUrl() != null) {
            post.setVideoUrl(request.getVideoUrl());
        }

        post.setUpdatedAt(LocalDateTime.now());

        return postMapper.toPostResponse(postRepository.save(post));
    }

    /**
     * Người dùng thích bài viết (tránh thích nhiều lần)
     */
    //Phương thức likePost(String postId, String userId) xử lý việc thích (like) hoặc bỏ thích (unlike) một bài viết.
    //Nếu người dùng đã thích bài viết trước đó, hệ thống sẽ xóa lượt thích (Unlike).
    //Nếu chưa thích, hệ thống sẽ thêm lượt thích (Like).
    @Transactional
    public PostResponse likePost(String postId, String userId) {
        //Tìm bài viết dựa trên postId
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        //Tìm người dùng dựa trên userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra xem người dùng đã thích bài viết hay chưa
        if (post.getLikedUsers().contains(user)) {
            // Nếu đã thích thì bỏ thích (Unlike)
            post.getLikedUsers().remove(user);
            //post.setLikeCount(post.getLikeCount() - 1);
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1)); // Đảm bảo không giảm dưới 0
        } else {
            // Nếu chưa thích thì thêm vào danh sách
            post.getLikedUsers().add(user);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public PostResponse savedPost(String postId, String userId) {
        // 1. Tìm user theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Tìm bài viết theo postId
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        // 3. Kiểm tra xem bài viết đã được lưu hay chưa
        boolean alreadySaved = user.getSavedPosts().stream()
                .anyMatch(savedPost -> savedPost.getPost().equals(post));

        if (alreadySaved) {
            throw new AppException(ErrorCode.POST_ALREADY_SAVED);
        }

        // 4. Tạo một đối tượng SavedPost và lưu vào danh sách
        SavedPost savedPost = new SavedPost();
        savedPost.setUser(user);
        savedPost.setPost(post);

        user.getSavedPosts().add(savedPost);
        savedPostRepository.save(savedPost);

        // 5. Trả về thông tin bài viết
        return postMapper.toPostResponse(post);
    }

}

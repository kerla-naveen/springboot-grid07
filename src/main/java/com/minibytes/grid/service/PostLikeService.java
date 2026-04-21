package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreatePostLikeRequest;
import com.minibytes.grid.entity.PostLike;
import com.minibytes.grid.exception.DuplicateResourceException;
import com.minibytes.grid.exception.ResourceNotFoundException;
import com.minibytes.grid.repository.PostLikeRepository;
import com.minibytes.grid.repository.PostRepository;
import com.minibytes.grid.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViralityService viralityService;

    public PostLike likePost(Long postId, CreatePostLikeRequest request) {
        log.info("User id={} liking postId={}", request.getUserId(), postId);

        if (!postRepository.existsById(postId)) {
            log.warn("Post not found with id={}", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        if (!userRepository.existsById(request.getUserId())) {
            log.warn("User not found with id={}", request.getUserId());
            throw new ResourceNotFoundException("User not found with id: " + request.getUserId());
        }

        if (postLikeRepository.existsByPostIdAndUserId(postId, request.getUserId())) {
            log.warn("User id={} already liked postId={}", request.getUserId(), postId);
            throw new DuplicateResourceException("User has already liked this post");
        }

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(request.getUserId())
                .build();

        PostLike saved = postLikeRepository.save(postLike);
        log.info("Post like recorded with id={}", saved.getId());

        viralityService.onHumanLike(postId);

        return saved;
    }
}

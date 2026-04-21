package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreatePostLikeRequest;
import com.minibytes.grid.entity.PostLike;
import com.minibytes.grid.exception.DuplicateResourceException;
import com.minibytes.grid.exception.ResourceNotFoundException;
import com.minibytes.grid.repository.PostLikeRepository;
import com.minibytes.grid.repository.PostRepository;
import com.minibytes.grid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostLike likePost(Long postId, CreatePostLikeRequest request) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        if (!userRepository.existsById(request.getUserId())) {
            throw new ResourceNotFoundException("User not found with id: " + request.getUserId());
        }

        if (postLikeRepository.existsByPostIdAndUserId(postId, request.getUserId())) {
            throw new DuplicateResourceException("User has already liked this post");
        }

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(request.getUserId())
                .build();

        return postLikeRepository.save(postLike);
    }
}

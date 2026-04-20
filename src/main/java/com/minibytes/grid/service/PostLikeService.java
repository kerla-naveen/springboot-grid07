package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreatePostLikeRequest;
import com.minibytes.grid.entity.PostLike;
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
        // Verify post exists
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
        
        // Verify user exists
        if (!userRepository.existsById(request.getUserId())) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }
        
        // Check if user already liked this post
        if (postLikeRepository.existsByPostIdAndUserId(postId, request.getUserId())) {
            throw new RuntimeException("User has already liked this post");
        }
        
        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(request.getUserId())
                .build();
        
        return postLikeRepository.save(postLike);
    }
}

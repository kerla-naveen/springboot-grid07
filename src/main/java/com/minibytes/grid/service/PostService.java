package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreatePostRequest;
import com.minibytes.grid.entity.Post;
import com.minibytes.grid.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(CreatePostRequest request) {
        Post post = Post.builder()
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        
        return postRepository.save(post);
    }
}

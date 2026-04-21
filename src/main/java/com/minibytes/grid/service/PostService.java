package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreatePostRequest;
import com.minibytes.grid.entity.Post;
import com.minibytes.grid.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(CreatePostRequest request) {
        log.info("Creating post for authorId={} authorType={}", request.getAuthorId(), request.getAuthorType());
        Post post = Post.builder()
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        Post saved = postRepository.save(post);
        log.info("Post created with id={}", saved.getId());
        return saved;
    }
}

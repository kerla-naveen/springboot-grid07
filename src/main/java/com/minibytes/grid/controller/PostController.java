package com.minibytes.grid.controller;

import com.minibytes.grid.dto.CreatePostRequest;
import com.minibytes.grid.entity.Post;
import com.minibytes.grid.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest request) {
        Post createdPost = postService.createPost(request);
        return ResponseEntity.ok(createdPost);
    }
}

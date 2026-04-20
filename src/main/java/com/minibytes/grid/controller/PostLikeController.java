package com.minibytes.grid.controller;

import com.minibytes.grid.dto.CreatePostLikeRequest;
import com.minibytes.grid.entity.PostLike;
import com.minibytes.grid.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/like")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<PostLike> likePost(@PathVariable Long postId, @RequestBody CreatePostLikeRequest request) {
        PostLike postLike = postLikeService.likePost(postId, request);
        return ResponseEntity.ok(postLike);
    }
}

package com.minibytes.grid.controller;

import com.minibytes.grid.dto.CreateCommentRequest;
import com.minibytes.grid.entity.Comment;
import com.minibytes.grid.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request) {
        Comment createdComment = commentService.createComment(postId, request);
        return ResponseEntity.ok(createdComment);
    }
}

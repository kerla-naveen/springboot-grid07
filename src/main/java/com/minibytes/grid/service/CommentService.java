package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateCommentRequest;
import com.minibytes.grid.entity.Comment;
import com.minibytes.grid.exception.ResourceNotFoundException;
import com.minibytes.grid.repository.CommentRepository;
import com.minibytes.grid.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment createComment(Long postId, CreateCommentRequest request) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        Comment comment = Comment.builder()
                .postId(postId)
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .depthLevel(request.getDepthLevel())
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }
}

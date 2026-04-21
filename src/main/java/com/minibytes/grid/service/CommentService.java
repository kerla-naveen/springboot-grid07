package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateCommentRequest;
import com.minibytes.grid.entity.Comment;
import com.minibytes.grid.exception.ResourceNotFoundException;
import com.minibytes.grid.repository.CommentRepository;
import com.minibytes.grid.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment createComment(Long postId, CreateCommentRequest request) {
        log.info("Adding comment to postId={} by authorId={} authorType={}", postId, request.getAuthorId(), request.getAuthorType());
        if (!postRepository.existsById(postId)) {
            log.warn("Post not found with id={}", postId);
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

        Comment saved = commentRepository.save(comment);
        log.info("Comment created with id={} depthLevel={}", saved.getId(), saved.getDepthLevel());
        return saved;
    }
}

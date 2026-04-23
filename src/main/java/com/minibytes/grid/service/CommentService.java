package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateCommentRequest;
import com.minibytes.grid.entity.Comment;
import com.minibytes.grid.entity.Post;
import com.minibytes.grid.exception.ResourceNotFoundException;
import com.minibytes.grid.repository.CommentRepository;
import com.minibytes.grid.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GuardrailService guardrailService;

    @Autowired
    private ViralityService viralityService;

    @Transactional
public Comment createComment(Long postId, CreateCommentRequest request) {
        log.info("Adding comment to postId={} by authorId={} authorType={}", postId, request.getAuthorId(), request.getAuthorType());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("Post not found with id={}", postId);
                    return new ResourceNotFoundException("Post not found with id: " + postId);
                });

        int depth = 0; // Top-level comments have depth 0
        // Vertical cap applies to all comments
        if(request.getParentId()!=null){
            Comment parentComment = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.getParentId()));
            depth = parentComment.getDepthLevel() + 1;
        }
        guardrailService.checkDepthLevel(depth);

        boolean isBotComment = "BOT".equalsIgnoreCase(request.getAuthorType());

        if (isBotComment) {
            // Cooldown checked first — no Redis side effects if it rejects
            if ("USER".equalsIgnoreCase(post.getAuthorType())) {
                guardrailService.checkAndSetCooldown(request.getAuthorId(), post.getAuthorId());
            }

            // Horizontal cap last — only increments after all other checks pass
            guardrailService.checkAndIncrementBotCount(postId);
        }


        Comment comment = Comment.builder()
                .postId(postId)
                .authorId(request.getAuthorId())
                .authorType(request.getAuthorType())
                .content(request.getContent())
                .depthLevel(depth)
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("Comment created with id={} depthLevel={}", saved.getId(), saved.getDepthLevel());

        // Virality scoring
        if (isBotComment) {
            viralityService.onBotReply(postId);
        } else {
            viralityService.onHumanComment(postId);
        }

        return saved;
    }
}

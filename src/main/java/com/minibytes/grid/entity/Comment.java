package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue
    Long id;

    Long postId;

    Long authorId;

    String authorType;

    String content;

    // parentId will be null, if it is direct comment to the post
    // if reply to another comment, parentId will be the id of the comment
    Long parentId;

    int depthLevel;
    LocalDateTime createdAt;
}

package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id @GeneratedValue
    Long id;

    Long postId;

    Long authorId;

    String authorType;

    String content;

    int depthLevel;

    LocalDateTime createdAt;
}

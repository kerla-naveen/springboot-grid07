package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue
    Long id;

    Long authorId;

    String authorType; // "USER" or "BOT"

    String content;

    LocalDateTime createdAt;
}

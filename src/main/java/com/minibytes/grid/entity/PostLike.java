package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PostLike {
    @Id
    @GeneratedValue
    Long id;

    Long postId;

    Long userId;
}

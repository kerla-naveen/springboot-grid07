package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Entity
public class User {
    @Id @GeneratedValue
    Long id;

    String username;

    boolean isPremium;
}
package com.minibytes.grid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Bot {
    @Id
    @GeneratedValue
    Long id;

    String name;

    String personaDescription;
}

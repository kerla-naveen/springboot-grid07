package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateUserRequest;
import com.minibytes.grid.entity.User;
import com.minibytes.grid.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(CreateUserRequest request) {
        log.info("Creating user with username={}", request.getUsername());
        User user = User.builder()
                .username(request.getUsername())
                .isPremium(request.isPremium())
                .build();
        User saved = userRepository.save(user);
        log.info("User created with id={}", saved.getId());
        return saved;
    }
}

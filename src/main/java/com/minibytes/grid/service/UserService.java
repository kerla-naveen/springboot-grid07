package com.minibytes.grid.service;

import com.minibytes.grid.dto.CreateUserRequest;
import com.minibytes.grid.entity.User;
import com.minibytes.grid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(CreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .isPremium(request.isPremium())
                .build();
        
        return userRepository.save(user);
    }
}

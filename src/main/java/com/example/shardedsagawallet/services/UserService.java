package com.example.shardedsagawallet.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shardedsagawallet.repositories.UserRepository;
import com.example.shardedsagawallet.schema.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class UserService {
    
    private final UserRepository userRepository;

    public User createUser(User user) {
        log.info("Creating user: {}", user);
        User newUser = userRepository.save(user);
        log.info("User created with id {} in database shardwallet{}", newUser.getId(), (newUser.getId() % 2 + 1));
        return newUser;
    } 

    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<User> getUsersByName(String name) {
        log.info("Fetching users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }
}

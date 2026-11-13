package com.example.shardedsagawallet.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shardedsagawallet.schema.User;
import com.example.shardedsagawallet.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequiredArgsConstructor 
@Slf4j 
@RequestMapping ("/api/v1/users")
public class UserController {
    
    private final UserService userService;

    @PostMapping 
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Received request to create user: {}", user);
        User newUser = userService.createUser(user);
        log.info("User created with id {} in database shardwallet{}", newUser.getId(), (newUser.getId() % 2 + 1));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Received request to fetch user with id: {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping ("/search")
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        log.info("Received request to fetch users with name containing: {}", name);
        List<User> users = userService.getUsersByName(name);
        return ResponseEntity.ok(users);
    }
}

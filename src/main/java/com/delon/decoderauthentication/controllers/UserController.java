package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.<ResponseEntity<Object>>map(entity -> ResponseEntity.status(HttpStatus.OK).body(entity))
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.map(entity -> {
            userService.delete(entity);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
}

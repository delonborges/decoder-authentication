package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.dtos.UserDTO;
import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private static final String IMAGE_UPDATED_SUCCESSFULLY = "Image updated successfully";
    private static final String MISMATCHED_OLD_PASSWORD = "Mismatched old password";
    private static final String PASSWORD_UPDATED_SUCCESSFULLY = "Password updated successfully";
    private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAllUsers(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserEntity> users = userService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.<ResponseEntity<Object>>map(entity -> ResponseEntity.status(HttpStatus.OK).body(entity))
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.map(entity -> {
            userService.delete(entity);
            return ResponseEntity.status(HttpStatus.OK).body(USER_DELETED_SUCCESSFULLY);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserById(@PathVariable(value = "userId") UUID userId,
                                                 @Validated(UserDTO.UserView.UserPut.class) @RequestBody @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.map(entity -> {
            entity.setFullName(userDTO.fullName());
            entity.setFullName(userDTO.fullName());
            entity.setFullName(userDTO.fullName());
            entity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(entity);
            return ResponseEntity.status(HttpStatus.OK).body(USER_UPDATED_SUCCESSFULLY);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePasswordById(@PathVariable(value = "userId") UUID userId,
                                                     @RequestBody @Validated(UserDTO.UserView.PasswordPut.class) @JsonView(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        if (userEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        } else if (!userEntity.get().getPassword().equals(userDTO.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(MISMATCHED_OLD_PASSWORD);
        } else {
            var userToUpdate = userEntity.get();
            userToUpdate.setPassword(userDTO.password());
            userToUpdate.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(PASSWORD_UPDATED_SUCCESSFULLY);
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<String> updateImageById(@PathVariable(value = "userId") UUID userId,
                                                  @RequestBody @Validated(UserDTO.UserView.ImagePut.class) @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.map(entity -> {
            entity.setImageUrl(userDTO.imageUrl());
            entity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(entity);
            return ResponseEntity.status(HttpStatus.OK).body(IMAGE_UPDATED_SUCCESSFULLY);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND));
    }
}

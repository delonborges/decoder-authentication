package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.constants.UserMessages;
import com.delon.decoderauthentication.dtos.UserDTO;
import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.services.UserService;
import com.delon.decoderauthentication.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                        @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug(UserMessages.LOG_GETTING_USERS_WITH_SPEC, spec);
        Page<UserEntity> users = userService.findAll(spec, pageable);
        users.toList().forEach(user -> user.add(linkTo(methodOn(UserController.class).getUserById(user.getUserId())).withSelfRel()));
        log.info(UserMessages.LOG_USERS_RETRIEVED, users.getNumberOfElements());
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        log.debug(UserMessages.LOG_FINDING_USER_BY_ID, userId);
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.<ResponseEntity<Object>>map(entity -> {
            log.info(UserMessages.LOG_USER_RETRIEVED);
            return ResponseEntity.status(HttpStatus.OK).body(entity);
        }).orElseGet(() -> {
            log.warn(UserMessages.LOG_USER_NOT_FOUND_BY_ID, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMessages.USER_NOT_FOUND);
        });
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        log.debug(UserMessages.LOG_FINDING_USER_BY_ID, userId);
        Optional<UserEntity> userEntity = userService.findById(userId);
        return userEntity.map(entity -> {
            log.debug(UserMessages.LOG_DELETING_USER_BY_ID, userId);
            userService.delete(entity);
            log.info(UserMessages.LOG_USER_DELETED);
            return ResponseEntity.status(HttpStatus.OK).body(UserMessages.USER_DELETED_SUCCESSFULLY);
        }).orElseGet(() -> {
            log.warn(UserMessages.LOG_USER_NOT_FOUND_BY_ID, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMessages.USER_NOT_FOUND);
        });
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserById(@PathVariable(value = "userId") UUID userId,
                                                 @Validated(UserDTO.UserView.UserPut.class) @RequestBody @JsonView(UserDTO.UserView.UserPut.class)
                                                 UserDTO userDTO) {
        log.debug(UserMessages.LOG_FINDING_USER_WITH_DATA, userDTO);
        Optional<UserEntity> userEntity = userService.findById(userId);
        log.debug(UserMessages.LOG_UPDATING_USER, userEntity);
        return userEntity.map(entity -> {
            entity.setFullName(userDTO.fullName());
            entity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            log.debug(UserMessages.LOG_SAVING_USER_ENTITY, entity);
            userService.save(entity);
            log.info(UserMessages.LOG_USER_UPDATED);
            return ResponseEntity.status(HttpStatus.OK).body(UserMessages.USER_UPDATED_SUCCESSFULLY);
        }).orElseGet(() -> {
            log.warn(UserMessages.LOG_USER_NOT_FOUND_WITH_DATA, userDTO);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMessages.USER_NOT_FOUND);
        });
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePasswordById(@PathVariable(value = "userId") UUID userId,
                                                     @RequestBody @Validated(UserDTO.UserView.PasswordPut.class) @JsonView(UserDTO.UserView.PasswordPut.class)
                                                     UserDTO userDTO) {
        log.debug(UserMessages.LOG_FINDING_USER_BY_ID, userId);
        Optional<UserEntity> userEntity = userService.findById(userId);
        log.debug(UserMessages.LOG_UPDATING_USER_PASSWORD, userEntity);
        if (userEntity.isEmpty()) {
            log.warn(UserMessages.LOG_USER_NOT_FOUND_BY_ID, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMessages.USER_NOT_FOUND);
        } else if (!userEntity.get().getPassword().equals(userDTO.oldPassword())) {
            log.warn(UserMessages.LOG_MISMATCHED_PASSWORD, userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserMessages.MISMATCHED_OLD_PASSWORD);
        } else {
            log.debug(UserMessages.LOG_UPDATING_USER_PASSWORD, userEntity);
            var userToUpdate = userEntity.get();
            userToUpdate.setPassword(userDTO.password());
            userToUpdate.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            log.debug(UserMessages.LOG_SAVING_USER_ENTITY, userToUpdate);
            userService.save(userToUpdate);
            log.info(UserMessages.LOG_USER_PASSWORD_UPDATED);
            return ResponseEntity.status(HttpStatus.OK).body(UserMessages.PASSWORD_UPDATED_SUCCESSFULLY);
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<String> updateImageById(@PathVariable(value = "userId") UUID userId,
                                                  @RequestBody @Validated(UserDTO.UserView.ImagePut.class) @JsonView(UserDTO.UserView.ImagePut.class)
                                                  UserDTO userDTO) {
        log.debug(UserMessages.LOG_FINDING_USER_BY_ID, userId);
        Optional<UserEntity> userEntity = userService.findById(userId);
        log.debug(UserMessages.LOG_UPDATING_USER_IMAGE, userEntity);
        return userEntity.map(entity -> {
            entity.setImageUrl(userDTO.imageUrl());
            entity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            log.debug(UserMessages.LOG_SAVING_USER_ENTITY, entity);
            userService.save(entity);
            log.info(UserMessages.LOG_USER_IMAGE_UPDATED);
            return ResponseEntity.status(HttpStatus.OK).body(UserMessages.IMAGE_UPDATED_SUCCESSFULLY);
        }).orElseGet(() -> {
            log.warn(UserMessages.LOG_USER_NOT_FOUND_BY_ID, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMessages.USER_NOT_FOUND);
        });
    }
}

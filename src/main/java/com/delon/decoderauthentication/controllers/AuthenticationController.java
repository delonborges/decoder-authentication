package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.dtos.UserDTO;
import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.enums.UserStatus;
import com.delon.decoderauthentication.enums.UserType;
import com.delon.decoderauthentication.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    private static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private static final String USERNAME_ALREADY_EXISTS = "Username already exists";

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(USERNAME_ALREADY_EXISTS);
        } else if (userService.existsByEmail(userDTO.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(EMAIL_ALREADY_EXISTS);
        } else {
            var userEntity = new UserEntity();
            BeanUtils.copyProperties(userDTO, userEntity);
            userEntity.setUserStatus(UserStatus.ACTIVE);
            userEntity.setUserType(UserType.STUDENT);
            userEntity.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userEntity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
        }
    }
}

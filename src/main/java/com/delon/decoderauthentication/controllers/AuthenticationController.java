package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.dtos.UserDTO;
import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.enums.UserStatus;
import com.delon.decoderauthentication.enums.UserType;
import com.delon.decoderauthentication.services.UserService;
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

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        } else if (userService.existsByEmail(userDTO.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } else {
            UserEntity newUser = createUserEntity(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
    }

    private UserEntity createUserEntity(UserDTO userDTO) {
        var currentTime = LocalDateTime.now(ZoneId.of("UTC"));
        var userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userEntity.setUserType(UserType.STUDENT);
        userEntity.setCreatedDate(currentTime);
        userEntity.setUpdatedDate(currentTime);
        userService.save(userEntity);
        return userEntity;
    }
}

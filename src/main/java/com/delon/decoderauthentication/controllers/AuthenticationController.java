package com.delon.decoderauthentication.controllers;

import com.delon.decoderauthentication.constants.AuthMessages;
import com.delon.decoderauthentication.dtos.UserDTO;
import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.enums.UserStatus;
import com.delon.decoderauthentication.enums.UserType;
import com.delon.decoderauthentication.services.iface.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    private final IUserService userService;

    public AuthenticationController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(
            @RequestBody @Validated(UserDTO.UserView.RegistrationPost.class) @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {
        log.debug(AuthMessages.LOG_SIGNING_UP_WITH_DATA, userDTO);
        if (userService.existsByUsername(userDTO.username())) {
            log.warn(AuthMessages.LOG_USERNAME_EXISTS, userDTO);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthMessages.USERNAME_ALREADY_EXISTS);
        } else if (userService.existsByEmail(userDTO.email())) {
            log.warn(AuthMessages.LOG_EMAIL_EXISTS, userDTO);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthMessages.EMAIL_ALREADY_EXISTS);
        } else {
            log.debug(AuthMessages.LOG_CREATING_NEW_USER);
            var userEntity = new UserEntity();
            BeanUtils.copyProperties(userDTO, userEntity);
            userEntity.setUserStatus(UserStatus.ACTIVE);
            userEntity.setUserType(UserType.STUDENT);
            userEntity.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            userEntity.setUpdatedDate(LocalDateTime.now(ZoneId.of("UTC")));
            log.debug(AuthMessages.LOG_SAVING_USER_TO_DB, userEntity);
            userService.save(userEntity);
            log.info(AuthMessages.LOG_USER_REGISTERED);
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
        }
    }
}

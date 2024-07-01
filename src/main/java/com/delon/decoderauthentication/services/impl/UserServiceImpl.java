package com.delon.decoderauthentication.services.impl;

import com.delon.decoderauthentication.entities.UserEntity;
import com.delon.decoderauthentication.repositories.UserRepository;
import com.delon.decoderauthentication.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}

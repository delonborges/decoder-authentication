package com.delon.decoderauthentication.services.iface;

import com.delon.decoderauthentication.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {

    Optional<UserEntity> findById(UUID userId);

    void delete(UserEntity userEntity);

    void save(UserEntity userEntity);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);
}

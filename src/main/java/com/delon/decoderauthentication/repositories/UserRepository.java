package com.delon.decoderauthentication.repositories;

import com.delon.decoderauthentication.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

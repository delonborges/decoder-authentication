package com.delon.decoderauthentication.repositories;

import com.delon.decoderauthentication.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {}

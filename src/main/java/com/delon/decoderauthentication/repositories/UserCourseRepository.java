package com.delon.decoderauthentication.repositories;

import com.delon.decoderauthentication.entities.UserCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseEntity, UUID> {
}

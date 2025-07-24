package com.delon.decoderauthentication.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_users_courses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCourseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8793505966272227660L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    private UUID courseId;
}

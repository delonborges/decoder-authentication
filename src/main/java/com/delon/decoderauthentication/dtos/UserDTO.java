package com.delon.decoderauthentication.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(String username,
                      String email,
                      String password,
                      String oldPassword,
                      String fullName,
                      String phoneNumber,
                      String cpf,
                      String imageUrl) {}

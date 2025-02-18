package com.delon.decoderauthentication.dtos;

import com.delon.decoderauthentication.validations.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(@UsernameConstraint(groups = UserView.RegistrationPost.class) @JsonView(UserView.RegistrationPost.class) String username,
                      @NotBlank(groups = UserView.RegistrationPost.class) @Size(min = 4,
                                                                                max = 50,
                                                                                groups = {UserView.RegistrationPost.class,
                                                                                          UserView.PasswordPut.class}) @Email @JsonView(UserView.RegistrationPost.class) String email,
                      @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class}) @JsonView({UserView.RegistrationPost.class,
                                                                                                                   UserView.PasswordPut.class}) String password,
                      @NotBlank(groups = UserView.PasswordPut.class) @Size(min = 4,
                                                                           max = 50,
                                                                           groups = UserView.PasswordPut.class) @JsonView(UserView.PasswordPut.class) String oldPassword,
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class}) String fullName,
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class}) String phoneNumber,
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class}) String cpf,
                      @NotBlank(groups = UserView.ImagePut.class) @JsonView(UserView.ImagePut.class) String imageUrl) {

    public interface UserView {
        interface RegistrationPost {}

        interface UserPut {}

        interface PasswordPut {}

        interface ImagePut {}
    }
}

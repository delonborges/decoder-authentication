package com.delon.decoderauthentication.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthMessages {

    public String EMAIL_ALREADY_EXISTS = "Email already exists";
    public String LOG_CREATING_NEW_USER = "Creating new user entity...";
    public String LOG_EMAIL_EXISTS = "Registration attempt with existing email: [{}]";
    public String LOG_SAVING_USER_TO_DB = "Saving user to database: [{}]";
    public String LOG_SIGNING_UP_WITH_DATA = "Signing up with user data: [{}]";
    public String LOG_USERNAME_EXISTS = "Registration attempt with existing username: [{}]";
    public String LOG_USER_REGISTERED = "User registered successfully.";
    public String USERNAME_ALREADY_EXISTS = "Username already exists";

}

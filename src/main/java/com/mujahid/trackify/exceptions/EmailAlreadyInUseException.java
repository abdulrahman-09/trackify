package com.mujahid.trackify.exceptions;

import lombok.Getter;

@Getter
public class EmailAlreadyInUseException extends ApiException{
    private final String email;

    public EmailAlreadyInUseException(String email) {
        super("User already exists with email: " + email);
        this.email = email;
    }
}

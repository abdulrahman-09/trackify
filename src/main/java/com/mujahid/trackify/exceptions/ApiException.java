package com.mujahid.trackify.exceptions;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}

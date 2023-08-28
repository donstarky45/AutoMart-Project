package com.Starky.codes.exceptions;

public class UserServiceException extends RuntimeException{
    private static final long serialVersionUiD = 1L;

    public UserServiceException(String message) {
        super(message);
    }
}

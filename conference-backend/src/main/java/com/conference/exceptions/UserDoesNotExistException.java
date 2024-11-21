package com.conference.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super("The user does not exist");
    }
}

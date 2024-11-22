package com.conference.exceptions;

public class IncorrectVerificationCodeException extends RuntimeException {

    public IncorrectVerificationCodeException() {
        super("The code passed did not match the users verification code");
    }
}

package com.conference.exceptions;

public class EmailFailedToSendException extends RuntimeException {
    public EmailFailedToSendException() {
        super("Email failed to send");
    }
}

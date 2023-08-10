package com.esl.internship.staffsync.commons.exceptions;

public class LoginAttemptExceededException extends Exception {
    public LoginAttemptExceededException(String message) {
        super(message);
    }
}
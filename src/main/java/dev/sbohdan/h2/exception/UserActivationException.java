package dev.sbohdan.h2.exception;

public class UserActivationException extends RuntimeException{
    public UserActivationException() {
    }

    public UserActivationException(String message) {
        super(message);
    }
}

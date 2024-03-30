package dev.sbohdan.h2.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}

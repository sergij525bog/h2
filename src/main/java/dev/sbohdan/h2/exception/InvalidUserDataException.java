package dev.sbohdan.h2.exception;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException() {
    }

    public InvalidUserDataException(String message) {
        super(message);
    }
}

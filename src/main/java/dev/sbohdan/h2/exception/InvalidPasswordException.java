package dev.sbohdan.h2.exception;

public class InvalidPasswordException extends RuntimeException {
    public static final String PASSWORD_REQUIREMENTS = """
            Password should have:
            length between 8 and 20 characters without whitespaces and include:
            at least one capital letter
            at least one lowercase letter
            at least one digit" +
            at least one special symbol @#$%^&+=]""";

    public InvalidPasswordException(String message) {
        super(message);
    }
}

package dev.sbohdan.h2.exceptionhandler;

import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserNotFoundException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserValidationExceptionHandler {
    @ExceptionHandler(InvalidPasswordException.class)
    ProblemDetail handleInvalidPasswordException(InvalidPasswordException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
        problemDetail.setTitle("Password input error");
        problemDetail.setProperty("requirements", InvalidPasswordException.PASSWORD_REQUIREMENTS);
        return problemDetail;
    }

    @ExceptionHandler(InvalidUserDataException.class)
    ProblemDetail handleInvalidUserDataException(InvalidUserDataException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
        problemDetail.setTitle("User data input error");
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
        problemDetail.setTitle("User already exists");
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    ProblemDetail handleUserNotFoundException(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
        problemDetail.setTitle("User does not found");
        return problemDetail;
    }
}

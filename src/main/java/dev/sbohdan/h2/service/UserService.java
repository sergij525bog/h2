package dev.sbohdan.h2.service;

import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import dev.sbohdan.h2.repository.UserRepository;
import dev.sbohdan.h2.utils.StringPatternValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(final UserDto newUser) {
        System.out.println(newUser);
        validateUser(newUser);
        userRepository.save(UserDto.toUser(newUser));
    }

    private void validateUser(final UserDto user) {
        validateUserData(user);
        checkPasswordIsConfirmed(user.getPassword(), user.getConfirmationPassword());
        validateUserNotAlreadyExists(user.getEmail());
    }

    private void validateUserData(final UserDto user) {
        validateName(user.getFirstName(), "First name");
        validateName(user.getLastName(), "Last name");
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    private void validateName(String name, String fieldName) {
        validateStringIsNotNullOrBlank(name, fieldName);
        if (!StringPatternValidationUtils.matchesName(name)) {
            throw new InvalidUserDataException(fieldName + " is invalid");
        }
    }

    private void validateEmail(String email) {
        validateStringIsNotNullOrBlank(email, "Email");
        if (!StringPatternValidationUtils.matchesEmail(email)) {
            throw new InvalidUserDataException("Email is invalid");
        }
    }

    private void validatePassword(String password) {
        validateStringIsNotNullOrBlank(password, "Password");
        final int length = password.length();
        if (length < 8 || length > 20) {
            throw new InvalidPasswordException("Password should has length between 8 and 20");
        }
        if (!StringPatternValidationUtils.matchesPassword(password)) {
            throw new InvalidPasswordException("Password is invalid");
        }
    }

    private void checkPasswordIsConfirmed(String password, String confirmationPassword) {
        if (!password.equals(confirmationPassword)) {
            throw new InvalidUserDataException("Password is not confirmed");
        }
    }

    private void validateStringIsNotNullOrBlank(final String string, final String fieldName) {
        if (string == null) {
            throw new InvalidUserDataException(fieldName + " is required!");
        }
        if (string.isBlank()) {
            throw new InvalidUserDataException(fieldName + " cannot be blank!");
        }
    }

    private void validateUserNotAlreadyExists(final String email) {
        final User savedUser = userRepository.findOptionalByEmail(email);
        if (savedUser != null) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists!");
        }
    }
}

package dev.sbohdan.h2.service;

import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import dev.sbohdan.h2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUer(final UserDto newUser) {
        System.out.println(newUser);
        validateUser(newUser);
        userRepository.save(UserDto.toUser(newUser));
    }

    private void validateUser(final UserDto user) {
        validateUserData(user);
        validateUserNotAlreadyExists(user.getEmail());
    }

    private void validateUserData(final UserDto user) {
        validateStringIsNotNullOrBlank(user.getFirstName(), "First name");
        validateStringIsNotNullOrBlank(user.getLastName(), "Last name");
        validateStringIsNotNullOrBlank(user.getEmail(), "Email");
        final String password = user.getPassword();
        validateStringIsNotNullOrBlank(password, "Password");
        if (password.length() < 8 || password.length() > 20) {
            throw new InvalidPasswordException("Password length should be between 8 and 20");
        }

        if (!password.equals(user.getConfirmationPassword())) {
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

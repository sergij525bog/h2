package dev.sbohdan.h2.service;

import dev.sbohdan.h2.event.UserRegistrationEvent;
import dev.sbohdan.h2.dto.ActivationCodeDto;
import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.ActivationCode;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserNotFoundException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import dev.sbohdan.h2.repository.ActivationCodeRepository;
import dev.sbohdan.h2.repository.UserRepository;
import dev.sbohdan.h2.utils.StringPatternValidationUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ActivationCodeRepository codeRepository;
    private final ApplicationEventPublisher publisher;

    public UserService(final UserRepository userRepository, ActivationCodeRepository codeRepository, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.publisher = publisher;
    }

    @Transactional
    public void addUser(final UserDto newUser) {
        UserValidator.validateUser(userRepository, newUser);

        final User inactiveUser = userRepository.save(UserDto.toUser(newUser));

        ActivationCode activationCode = new ActivationCode();
        final String code = UUID.randomUUID().toString();
        activationCode.setCode(code);
        activationCode.setUser(inactiveUser);

        ActivationCode savedCode = codeRepository.save(activationCode);

        publisher.publishEvent(new UserRegistrationEvent(inactiveUser, savedCode.getCode()));
    }

    @Transactional
    public void activateUser(ActivationCodeDto activationCode) {
        String code = activationCode.getActivationCode();
        Long userId = codeRepository.findUserIdByCode(code);

        if (userId == null) {
            throw new UserNotFoundException("User with activation code " + code + " does not found");
        }

        userRepository.updateActivatedById(userId);
    }

    private static class UserValidator {
        private UserValidator() {}

        public static void validateUser(final UserRepository userRepository, final UserDto user) {
            validateUserData(user);
            checkPasswordIsConfirmed(user.getPassword(), user.getConfirmationPassword());
            validateUserNotAlreadyExists(userRepository, user.getEmail());
        }

        private static void validateUserData(final UserDto user) {
            validateName(user.getFirstName(), "First name");
            validateName(user.getLastName(), "Last name");
            validateEmail(user.getEmail());
            validatePassword(user.getPassword());
        }

        private static void validateName(String name, String fieldName) {
            validateStringIsNotNullOrBlank(name, fieldName);
            if (!StringPatternValidationUtils.matchesName(name)) {
                throw new InvalidUserDataException(fieldName + " is invalid");
            }
        }

        private static void validateEmail(String email) {
            validateStringIsNotNullOrBlank(email, "Email");

            if (!StringPatternValidationUtils.matchesEmail(email)) {
                throw new InvalidUserDataException("Email is invalid");
            }
        }

        private static void validatePassword(String password) {
            validateStringIsNotNullOrBlank(password, "Password");

            final int length = password.length();
            if (length < 8 || length > 20) {
                throw new InvalidPasswordException("Password should has length between 8 and 20");
            }

            if (!StringPatternValidationUtils.matchesPassword(password)) {
                throw new InvalidPasswordException("Password is invalid");
            }
        }

        private static void checkPasswordIsConfirmed(String password, String confirmationPassword) {
            if (!password.equals(confirmationPassword)) {
                throw new InvalidUserDataException("Password is not confirmed");
            }
        }

        private static void validateStringIsNotNullOrBlank(final String string, final String fieldName) {
            if (string == null) {
                throw new InvalidUserDataException(fieldName + " is required!");
            }
            if (string.isBlank()) {
                throw new InvalidUserDataException(fieldName + " cannot be blank!");
            }
        }

        private static void validateUserNotAlreadyExists(final UserRepository userRepository, final String email) {
            if (userRepository.existsByEmail(email)) {
                throw new UserAlreadyExistsException("User with email " + email + " already exists!");
            }
        }
    }
}

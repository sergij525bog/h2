package dev.sbohdan.h2.service;

import dev.sbohdan.h2.UserRegistrationEvent;
import dev.sbohdan.h2.dto.ActivationCodeDto;
import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.ActivationCode;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserActivationException;
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
        validateUser(newUser);

        final User inactiveUser = userRepository.save(UserDto.toUser(newUser));

        ActivationCode activationCode = new ActivationCode();
        final String code = UUID.randomUUID().toString();
        activationCode.setCode(code);
        activationCode.setUser(inactiveUser);

        codeRepository.save(activationCode);

        publisher.publishEvent(new UserRegistrationEvent(inactiveUser, code));
    }

    @Transactional
    public void activateUser(ActivationCodeDto activationCode) {
        Long userId = codeRepository.findUserIdByCode(activationCode.getActivationCode());

        if (userId == null) {
            throw new UserActivationException("User with activation code " + activationCode + " does not found");
        }

        userRepository.updateActivatedById(userId);
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
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists!");
        }
    }
}

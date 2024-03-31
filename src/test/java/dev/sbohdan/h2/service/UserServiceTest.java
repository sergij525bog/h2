package dev.sbohdan.h2.service;

import dev.sbohdan.h2.dto.ActivationCodeDto;
import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.ActivationCode;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import dev.sbohdan.h2.exception.UserNotFoundException;
import dev.sbohdan.h2.repository.ActivationCodeRepository;
import dev.sbohdan.h2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepositoryMock;

    @MockBean
    private ActivationCodeRepository codeRepository;

    @Autowired
    private UserService userService;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    public void itShouldFailIfUserHasNullOrBlankFields(String value) {
        final UserDto invalidUser = new UserDto.UserDtoBuilder()
                .firstName(value)
                .email(value)
                .password(value)
                .confirmationPassword(value)
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.addUser(invalidUser));
    }

    @Test
    public void isShouldFailIfPasswordIsNotConfirmed() {
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("name")
                .email("email")
                .password("password")
                .confirmationPassword("other password")
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.addUser(validUser));
    }

    @Test
    public void itShouldFailIfUserAlreadyExists() {
        final String email = "email@email.com";
        final String newUserPassword = "P2ssw^rd2egq";
        final UserDto newUser = new UserDto.UserDtoBuilder()
                .firstName("Othername")
                .lastName("Othername")
                .email(email)
                .password(newUserPassword)
                .confirmationPassword(newUserPassword)
                .build();

        when(userRepositoryMock.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(newUser));
    }

    @Test
    public void itShouldFailIfPasswordHasWrongLength() {
        String shortPassword = "1234567";
        String longPassword = "123456789012345678901";
        List<UserDto> users = Stream.of(shortPassword, longPassword)
                .map(p -> new UserDto.UserDtoBuilder()
                        .firstName("Firstname")
                        .lastName("Lastname")
                        .email("email@email.com")
                        .password(p)
                        .confirmationPassword(p)
                        .build())
                .toList();

        users.forEach(user -> assertThrows(InvalidPasswordException.class, () -> userService.addUser(user)));
    }

    @Test
    public void isShouldSaveUserSuccessfully() {
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email@email.com")
                .password("P2ssw^rd")
                .confirmationPassword("P2ssw^rd")
                .build();
        final User user = UserDto.toUser(validUser);
        user.setId(1L);

        when(userRepositoryMock.save(any())).thenReturn(user);
        when(codeRepository.save(any())).thenReturn(new ActivationCode("code"));

        assertDoesNotThrow(() -> userService.addUser(validUser));
    }

    @Test
    public void itShouldFailIfActivationCodeWasNotFound() {
        ActivationCodeDto invalidCode = new ActivationCodeDto("invalid code");
        ActivationCode validCode = new ActivationCode("valid code");
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email@email.com")
                .password("P2ssw^rd")
                .confirmationPassword("P2ssw^rd")
                .build();
        final User user = UserDto.toUser(validUser);
        user.setId(1L);

        when(userRepositoryMock.save(any())).thenReturn(user);
        when(codeRepository.save(any())).thenReturn(validCode);
        when(codeRepository.findUserIdByCode(invalidCode.getActivationCode())).thenReturn(null);

        userService.addUser(validUser);

        assertThrows(UserNotFoundException.class, () -> userService.activateUser(invalidCode));
    }

    @Test
    public void isShouldActivateUserSuccessfully() {
        ActivationCodeDto validCodeDto = new ActivationCodeDto("invalid code");
        ActivationCode validCode = new ActivationCode("valid code");
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("email@email.com")
                .password("P2ssw^rd")
                .confirmationPassword("P2ssw^rd")
                .build();
        final User user = UserDto.toUser(validUser);
        user.setId(1L);

        when(userRepositoryMock.save(any())).thenReturn(user);
        when(codeRepository.save(any())).thenReturn(validCode);
        when(codeRepository.findUserIdByCode(validCodeDto.getActivationCode())).thenReturn(user.getId());

        userService.addUser(validUser);

        assertDoesNotThrow(() -> userService.activateUser(validCodeDto));
    }
}

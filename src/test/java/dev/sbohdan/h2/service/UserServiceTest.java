package dev.sbohdan.h2.service;

import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.entity.User;
import dev.sbohdan.h2.exception.InvalidPasswordException;
import dev.sbohdan.h2.exception.InvalidUserDataException;
import dev.sbohdan.h2.exception.UserAlreadyExistsException;
import dev.sbohdan.h2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepositoryMock;

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

        assertThrows(InvalidUserDataException.class, () -> userService.addUer(invalidUser));
    }

    @Test
    public void isShouldFailIfPasswordIsNotConfirmed() {
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("name")
                .email("email")
                .password("password")
                .confirmationPassword("other password")
                .build();

        assertThrows(InvalidUserDataException.class, () -> userService.addUer(validUser));
    }

    @Test
    public void itShouldFailIfUserAlreadyExists() {
        final String email = "email";
        final User userFromDb = new User.UserBuilder()
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .password("password")
                .build();
        final String newUserPassword = "other password";
        final UserDto newUser = new UserDto.UserDtoBuilder()
                .firstName("other name")
                .lastName("other name")
                .email(email)
                .password(newUserPassword)
                .confirmationPassword(newUserPassword)
                .build();

        when(userRepositoryMock.findOptionalByEmail(email)).thenReturn(userFromDb);

        assertThrows(UserAlreadyExistsException.class, () -> userService.addUer(newUser));
    }

    @Test
    public void itShouldFailIfPasswordHasWrongLength() {
        String shortPassword = "1234567";
        String longPassword = "123456789012345678901";
        List<UserDto> users = Stream.of(shortPassword, longPassword)
                .map(p -> new UserDto.UserDtoBuilder()
                        .firstName("firstname")
                        .lastName("lastname")
                        .email("email")
                        .password(p)
                        .confirmationPassword(p)
                        .build())
                .toList();

        users.forEach(user -> assertThrows(InvalidPasswordException.class, () -> userService.addUer(user)));
    }

    @Test
    public void isShouldSaveUserSuccessfully() {
        final UserDto validUser = new UserDto.UserDtoBuilder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("password")
                .confirmationPassword("password")
                .build();

        assertDoesNotThrow(() -> userService.addUer(validUser));
    }
}

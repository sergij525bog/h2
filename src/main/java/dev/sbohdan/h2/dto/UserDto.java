package dev.sbohdan.h2.dto;

import dev.sbohdan.h2.entity.User;

public final class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmationPassword;

    public UserDto() {
    }

    public UserDto(
            final String firstName,
            final String lastName,
            final String email,
            final String password,
            final String confirmationPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(final String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public static User toUser(final UserDto dto) {
        return new User.UserBuilder()
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .email(dto.email)
                .password(dto.password)
                .build();
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmationPassword='" + confirmationPassword + '\'' +
                '}';
    }

    public static final class UserDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String confirmationPassword;

        public UserDtoBuilder() {}

        public UserDtoBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDtoBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDtoBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder confirmationPassword(final String confirmationPassword) {
            this.confirmationPassword = confirmationPassword;
            return this;
        }

        public UserDto build() {
            return new UserDto(firstName, lastName, email, password, confirmationPassword);
        }
    }
}

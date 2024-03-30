package dev.sbohdan.h2.entity;

import dev.sbohdan.h2.exception.InvalidUserDataException;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean activated;

    public User() {
        activated = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(final boolean activated) {
        this.activated = activated;
    }

    public static final class UserBuilder {
        private String firstName;
        private String lastname;
        private String email;
        private String password;

        public UserBuilder() {}

        public UserBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(final String lastName) {
            this.lastname = lastName;
            return this;
        }

        public UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public User build() {
            if (firstName == null || lastname == null || email == null || password == null) {
                throw new InvalidUserDataException("Required user properties cannot be null");
            }

            User user = new User();
            user.firstName = this.firstName;
            user.lastName = this.lastname;
            user.email = this.email;
            user.password = this.password;

            return user;
        }
    }
}

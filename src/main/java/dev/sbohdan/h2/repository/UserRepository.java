package dev.sbohdan.h2.repository;

import dev.sbohdan.h2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOptionalByEmail(final String email);
}

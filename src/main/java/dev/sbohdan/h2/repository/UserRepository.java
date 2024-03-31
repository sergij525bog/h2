package dev.sbohdan.h2.repository;

import dev.sbohdan.h2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(final String email);
    @Modifying
    @Query("update User u set u.activated = true where u.id = :id")
    void updateActivatedById(Long id);
}

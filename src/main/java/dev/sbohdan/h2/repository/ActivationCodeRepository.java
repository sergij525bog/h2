package dev.sbohdan.h2.repository;

import dev.sbohdan.h2.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    @Query("select ac.user.id from ActivationCode ac where ac.code = :code")
    Long findUserIdByCode(@Param("code") final String code);
}

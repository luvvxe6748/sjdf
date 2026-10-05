package io.luv.pulsehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.luv.pulsehub.domain.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    boolean existsByUsername(String username);
}

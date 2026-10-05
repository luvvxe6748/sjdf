package io.luv.pulsehub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.luv.pulsehub.domain.Role;
import io.luv.pulsehub.repository.RoleRepository;
import io.luv.pulsehub.repository.UserAccountRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserManagementServiceTest {
    @Mock UserAccountRepository users;
    @Mock RoleRepository roles;
    @Mock PasswordEncoder encoder;
    @Mock AuditService audit;
    @InjectMocks DefaultUserManagementService service;

    @Test
    void shouldRejectDuplicateUsername() {
        when(users.existsByUsername("student")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> service.create("Student", "Иван Иванов", "secret1", 1));
    }

    @Test
    void shouldRejectUnknownRole() {
        when(users.existsByUsername("student")).thenReturn(false);
        when(roles.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.create("student", "Иван Иванов", "secret1", 99));
    }
}

package io.luv.pulsehub.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.luv.pulsehub.domain.UserAccount;
import io.luv.pulsehub.repository.RoleRepository;
import io.luv.pulsehub.repository.UserAccountRepository;

import java.util.List;

@Service
public class DefaultUserManagementService implements UserManagementService {
    private final UserAccountRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final AuditService audit;

    public DefaultUserManagementService(UserAccountRepository users, RoleRepository roles,
                                        PasswordEncoder passwordEncoder, AuditService audit) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
        this.audit = audit;
    }

    @Override
    public List<UserAccount> findAll() {
        return users.findAll();
    }

    @Override
    public UserAccount findById(long id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public UserAccount create(String username, String fullName, String password, long roleId) {
        if (users.existsByUsername(username.trim().toLowerCase())) {
            throw new IllegalArgumentException("Такой логин уже занят");
        }
        var role = roles.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));
        var saved = users.save(new UserAccount(username, fullName, passwordEncoder.encode(password), role));
        audit.record("USER_CREATED", "Создан пользователь " + saved.getUsername());
        return saved;
    }

    @Override
    @Transactional
    public UserAccount update(long id, String fullName, String password, long roleId, boolean enabled) {
        var user = findById(id);
        var role = roles.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));
        user.update(fullName, role, enabled);
        if (password != null && !password.isBlank()) {
            user.changePassword(passwordEncoder.encode(password));
        }
        audit.record("USER_UPDATED", "Изменён пользователь " + user.getUsername());
        return user;
    }

    @Override
    @Transactional
    public void delete(long id, String currentUsername) {
        var user = findById(id);
        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Нельзя удалить свою учётную запись");
        }
        users.delete(user);
        audit.record("USER_DELETED", "Удалён пользователь " + user.getUsername());
    }
}

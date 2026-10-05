package io.luv.pulsehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.luv.pulsehub.domain.Permission;
import io.luv.pulsehub.domain.Role;
import io.luv.pulsehub.repository.RoleRepository;
import io.luv.pulsehub.repository.UserAccountRepository;

import java.util.List;
import java.util.Set;

@Service
public class DefaultRoleManagementService implements RoleManagementService {
    private final RoleRepository roles;
    private final UserAccountRepository users;
    private final AuditService audit;

    public DefaultRoleManagementService(RoleRepository roles, UserAccountRepository users, AuditService audit) {
        this.roles = roles;
        this.users = users;
        this.audit = audit;
    }

    @Override
    public List<Role> findAll() { return roles.findAll(); }

    @Override
    public Role findById(long id) {
        return roles.findById(id).orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));
    }

    @Override
    @Transactional
    public Role create(String name, String description, Set<Permission> permissions) {
        var normalized = name.trim().toUpperCase().replace(' ', '_');
        if (roles.existsByName(normalized)) throw new IllegalArgumentException("Такая роль уже существует");
        var role = roles.save(new Role(name, description, permissions));
        audit.record("ROLE_CREATED", "Создана роль " + role.getName());
        return role;
    }

    @Override
    @Transactional
    public Role update(long id, String name, String description, Set<Permission> permissions) {
        var role = findById(id);
        role.update(name, description, permissions);
        audit.record("ROLE_UPDATED", "Изменена роль " + role.getName());
        return role;
    }

    @Override
    @Transactional
    public void delete(long id) {
        var role = findById(id);
        boolean used = users.findAll().stream().anyMatch(user -> user.getRole().getId().equals(id));
        if (used) throw new IllegalArgumentException("Роль назначена пользователям");
        roles.delete(role);
        audit.record("ROLE_DELETED", "Удалена роль " + role.getName());
    }
}

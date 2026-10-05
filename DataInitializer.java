package io.luv.pulsehub.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.luv.pulsehub.domain.Permission;
import io.luv.pulsehub.domain.Role;
import io.luv.pulsehub.domain.UserAccount;
import io.luv.pulsehub.repository.RoleRepository;
import io.luv.pulsehub.repository.UserAccountRepository;

import java.util.EnumSet;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(RoleRepository roles, UserAccountRepository users, PasswordEncoder encoder) {
        return args -> {
            if (roles.count() > 0) return;
            var hubadmin = roles.save(new Role("NEXUS", "Владелец системы", EnumSet.allOf(Permission.class)));
            roles.save(new Role("TEAM_LEAD", "Управление профилями сотрудников",
                    EnumSet.of(Permission.USERS_VIEW, Permission.USERS_MANAGE)));
            roles.save(new Role("VIEWER", "Контроль прав и журнала",
                    EnumSet.of(Permission.USERS_VIEW, Permission.ROLES_VIEW, Permission.AUDIT_VIEW)));
            users.save(new UserAccount("hubadmin", "Владелец портала", encoder.encode("hub2026"), hubadmin));
        };
    }
}

package io.luv.pulsehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import io.luv.pulsehub.repository.UserAccountRepository;

import java.util.ArrayList;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/login", "/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .exceptionHandling(errors -> errors.accessDeniedPage("/access-denied"))
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(UserAccountRepository users) {
        return username -> {
            var account = users.findByUsername(username.toLowerCase())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole().getName()));
            account.getRole().getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.name())));
            return new User(account.getUsername(), account.getPassword(), account.isEnabled(),
                    true, true, true, authorities);
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

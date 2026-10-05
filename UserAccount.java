package io.luv.pulsehub.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    protected UserAccount() {
    }

    public UserAccount(String username, String fullName, String password, Role role) {
        this.username = username.trim().toLowerCase();
        this.fullName = fullName.trim();
        this.password = password;
        this.role = role;
    }

    public void update(String fullName, Role role, boolean enabled) {
        this.fullName = fullName.trim();
        this.role = role;
        this.enabled = enabled;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public boolean isEnabled() { return enabled; }
    public Role getRole() { return role; }
}

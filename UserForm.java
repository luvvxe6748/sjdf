package io.luv.pulsehub.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserForm {
    private Long id;
    @NotBlank(message = "Введите логин")
    @Size(min = 3, max = 40, message = "От 3 до 40 символов")
    private String username;
    @NotBlank(message = "Введите имя")
    @Size(max = 100)
    private String fullName;
    private String password;
    @NotNull(message = "Выберите роль")
    private Long roleId;
    private boolean enabled = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

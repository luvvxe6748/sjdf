package io.luv.pulsehub.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.luv.pulsehub.domain.Permission;

import java.util.LinkedHashSet;
import java.util.Set;

public class RoleForm {
    private Long id;
    @NotBlank(message = "Введите название")
    @Size(max = 50)
    private String name;
    @NotBlank(message = "Введите описание")
    @Size(max = 120)
    private String description;
    private Set<Permission> permissions = new LinkedHashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
}

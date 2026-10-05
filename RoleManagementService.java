package io.luv.pulsehub.service;

import io.luv.pulsehub.domain.Permission;
import io.luv.pulsehub.domain.Role;

import java.util.List;
import java.util.Set;

public interface RoleManagementService {
    List<Role> findAll();
    Role findById(long id);
    Role create(String name, String description, Set<Permission> permissions);
    Role update(long id, String name, String description, Set<Permission> permissions);
    void delete(long id);
}

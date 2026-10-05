package io.luv.pulsehub.service;

import io.luv.pulsehub.domain.UserAccount;

import java.util.List;

public interface UserManagementService {
    List<UserAccount> findAll();
    UserAccount findById(long id);
    UserAccount create(String username, String fullName, String password, long roleId);
    UserAccount update(long id, String fullName, String password, long roleId, boolean enabled);
    void delete(long id, String currentUsername);
}

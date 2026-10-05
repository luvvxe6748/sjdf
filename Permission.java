package io.luv.pulsehub.domain;

public enum Permission {
    USERS_VIEW("Просмотр пользователей"),
    USERS_MANAGE("Управление пользователями"),
    ROLES_VIEW("Просмотр ролей"),
    ROLES_MANAGE("Управление ролями"),
    AUDIT_VIEW("Просмотр журнала действий");

    private final String title;

    Permission(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

package com.example.ears.dto;

public enum Permission {
    JOB_READ("job:read"),
    JOB_CREATE("job:create"),
    JOB_UPDATE("job:update"),
    JOB_DELETE("job:delete"),
    APPLICATION_CREATE("application:create"),
    APPLICATION_READ("application:read"),
    COMPANY_MANAGE("company:manage"),
    PROFILE_MANAGE("profile:manage");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

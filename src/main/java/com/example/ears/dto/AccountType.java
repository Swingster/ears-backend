package com.example.ears.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum AccountType {
    APPLICANT(Set.of(
            Permission.APPLICATION_READ,
            Permission.APPLICATION_CREATE,
            Permission.JOB_READ,
            Permission.PROFILE_MANAGE
    )),
    EMPLOYER(Set.of(
            Permission.JOB_READ,
            Permission.JOB_CREATE,
            Permission.JOB_UPDATE,
            Permission.JOB_DELETE,
            Permission.APPLICATION_READ,
            Permission.COMPANY_MANAGE,
            Permission.PROFILE_MANAGE
    ));

    private final Set<Permission> permissions;

    AccountType(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public List<GrantedAuthority> getAuthorities() {
       List<GrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
               .collect(Collectors.toList());
       authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

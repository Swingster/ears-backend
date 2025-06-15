package com.example.ears.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private Long id;
    private String email;
    private String fullName;
    private String accountType;
    private boolean enabled;

    public UserProfileDto() {}

    public UserProfileDto(Long id, String email, String fullName, String accountType, boolean enabled) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.accountType = accountType;
        this.enabled = enabled;
    }
}

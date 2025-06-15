package com.example.ears.responses;

import com.example.ears.dto.UserProfileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private String accountType;
    private List<String> permissions;
    private UserProfileDto user;

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}

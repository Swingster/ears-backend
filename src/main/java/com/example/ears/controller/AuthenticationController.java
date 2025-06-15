package com.example.ears.controller;

import com.example.ears.dto.*;
import com.example.ears.model.User;
import com.example.ears.responses.LoginResponse;
import com.example.ears.service.AuthenticationService;
import com.example.ears.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        User authenticatedUser = authenticationService.authentication(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpiration());
        loginResponse.setAccountType(authenticatedUser.getAccountType().toString());

        //Set user profile information
        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setEmail(authenticatedUser.getEmail());
        userProfile.setFullName(authenticatedUser.getUsername());
        userProfile.setAccountType(authenticatedUser.getAccountType().toString());
        userProfile.setEnabled(authenticatedUser.isEnabled());
        loginResponse.setUser(userProfile);

        //Set Permissions based on account type
        List<String> permissions = new ArrayList<>();
        if (authenticatedUser.getAccountType() == AccountType.EMPLOYER) {
            permissions.add("POST_JOBS");
            permissions.add("VIEW_APPLICATIONS");
            permissions.add("MANAGE_COMPANY_PROFILE");
        } else if (authenticatedUser.getAccountType() == AccountType.APPLICANT) {
            permissions.add("APPLY_JOBS");
            permissions.add("MANAGE_RESUME");
            permissions.add("VIEW_APPLICATION_STATUS");
        }
        loginResponse.setPermissions(permissions);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

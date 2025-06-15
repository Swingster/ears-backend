package com.example.ears.controller;

import com.example.ears.dto.AccountType;
import com.example.ears.model.User;
import com.example.ears.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Enhanced response with role-specific information
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", currentUser.getId());
        userInfo.put("email", currentUser.getEmail());
        userInfo.put("accountType", currentUser.getAccountType());
        userInfo.put("isEnabled", currentUser.isEnabled());

        // Add role-specific information
        if (currentUser.getAccountType() == AccountType.EMPLOYER) {
            // For employers, you might add company info, active job postings count, etc.
            userInfo.put("canPostJobs", true);
        } else if (currentUser.getAccountType() == AccountType.APPLICANT) {
            // For applicants, you might add resume status, application count, etc.
            userInfo.put("canApplyToJobs", true);
        }

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/employers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allEmployers() {
        // Admin endpoint to see all employers
        List<User> employers = userService.getUsersByAccountType(AccountType.EMPLOYER);
        return ResponseEntity.ok(employers);
    }

    @GetMapping("/applicants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allApplicants() {
        // Admin endpoint to see all applicants
        List<User> applicants = userService.getUsersByAccountType(AccountType.APPLICANT);
        return ResponseEntity.ok(applicants);
    }
}

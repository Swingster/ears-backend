package com.example.ears.service;

import com.example.ears.dto.AccountType;
import com.example.ears.model.User;
import com.example.ears.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> getUsersByAccountType(AccountType accountType) {
        return userRepository.findByAccountType(accountType);
    }

    public List<User> getAllEmoloyers() {
        return getUsersByAccountType(AccountType.EMPLOYER);
    }

    public List<User> getAllApplicants() {
        return getUsersByAccountType(AccountType.APPLICANT);
    }
}

package com.example.ears.repository;

import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
}

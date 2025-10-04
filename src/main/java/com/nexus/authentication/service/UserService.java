package com.nexus.authentication.service;

import com.nexus.authentication.model.User;
import com.nexus.authentication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private final UserRepository repo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUser() {
        return repo.findAll();
    }
    public User register(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }


    public User updateUser(Long id, User userDetails) {
        return repo.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDetails.getName());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setPassword(userDetails.getPassword());
                    return repo.save(existingUser);   // save updated user
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public String deleteUser(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "User with id " + id + " deleted successfully";
        }
        return "User not found";
    }
    // authentication
    public User authenticate(String email, String rawPassword) {
        return repo.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .orElse(null);
    }

    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }


}

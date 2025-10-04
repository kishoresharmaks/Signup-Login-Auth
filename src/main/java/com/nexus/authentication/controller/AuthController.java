package com.nexus.authentication.controller;

import com.nexus.authentication.model.User;
import com.nexus.authentication.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Signup
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return "Email and password required!";
        }
        if (userService.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }
        userService.register(user);
        return "Signup successful!";
    }


    // Login
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession session) {
        User dbUser = userService.authenticate(user.getEmail(), user.getPassword());
        if (dbUser == null) {
            return "Invalid credentials!";
        }
        session.setAttribute("userId", dbUser.getId());
        session.setAttribute("username", dbUser.getName());
        return "Login successful!";
    }

    // Profile
    @GetMapping("/me")
    public String me(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        if (userId == null) {
            return "Not logged in!";
        }
        return "Welcome," + username;
    }

    // Logout
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out!";
    }
}

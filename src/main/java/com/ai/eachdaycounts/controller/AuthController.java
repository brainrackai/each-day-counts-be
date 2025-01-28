package com.ai.eachdaycounts.controller;

import com.ai.eachdaycounts.dto.AuthResponse;
import com.ai.eachdaycounts.entity.User;
import com.ai.eachdaycounts.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authservice;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user){
        AuthResponse response = authservice.registerUser(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user, HttpServletResponse response) {
        AuthResponse authResponse = authservice.loginUser(user.getEmail(), user.getPassword());
        
        Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
        jwtCookie.setHttpOnly(true);  // Prevents JavaScript access
        jwtCookie.setSecure(true);    // Only sends over HTTPS
        jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
        jwtCookie.setPath("/");       // Cookie is valid for all paths
        
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = authservice.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/naush")
    public ResponseEntity<User> getNaushEntry(){
        User user = authservice.getNaushEntry();
        return ResponseEntity.ok(user);
    }
}

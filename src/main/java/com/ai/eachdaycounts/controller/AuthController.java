package com.ai.eachdaycounts.controller;

import com.ai.eachdaycounts.dto.ApiResponse;
import com.ai.eachdaycounts.dto.AuthResponse;
import com.ai.eachdaycounts.entity.User;
import com.ai.eachdaycounts.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
    origins = {"http://localhost:3000", "http://localhost:4200"}, 
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authservice;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@RequestBody User user) {
        logger.info("Registration attempt for user: {}", user.getEmail());
        AuthResponse response = authservice.registerUser(user.getEmail(), user.getPassword());
        logger.debug("Registration successful for user: {}", user.getEmail());
        return ResponseEntity.ok(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody User user, HttpServletResponse response) {
        logger.info("Login attempt for user: {}", user.getEmail());
        AuthResponse authResponse = authservice.loginUser(user.getEmail(), user.getPassword());
        
        Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
        logger.info("Login attempt for user: {}", authResponse.getToken());

        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);  // Set to true in production with HTTPS
        jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
        jwtCookie.setPath("/");
        jwtCookie.setDomain("localhost"); // Set your domain
        
        // Add SameSite attribute through header since Jakarta Cookie doesn't support it directly
        String sameSiteCookieString = String.format("jwt=%s; Max-Age=%d; Path=%s; HttpOnly; SameSite=Lax", 
            authResponse.getToken(), 
            jwtCookie.getMaxAge(), 
            jwtCookie.getPath());
        response.addHeader("Set-Cookie", sameSiteCookieString);
        
        logger.debug("Login successful for user: {}", user.getEmail());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getUsers() {
        logger.info("Retrieving all users");
        List<User> users = authservice.getUsers();
        logger.debug("Successfully retrieved {} users", users.size());
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    @GetMapping("/naush")
    public ResponseEntity<ApiResponse<User>> getNaushEntry() {
        User user = authservice.getNaushEntry();
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
}

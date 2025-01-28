package com.ai.eachdaycounts.service;

import com.ai.eachdaycounts.dto.AuthResponse;
import com.ai.eachdaycounts.entity.User;
import com.ai.eachdaycounts.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse registerUser(String email, String password) {
        logger.info("Starting user registration process for email: {}", email);
        try {
            if(userRepository.existsByEmail((email))){
                throw new RuntimeException("Email already exists!!");
            }

            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("USER");
            userRepository.save(user);
            
            String token = jwtService.generateToken(email, user.getRole());
            logger.debug("User registration completed successfully for: {}", email);
            return new AuthResponse(token, user.getRole());
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            throw e;
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getNaushEntry() {
        return userRepository.findNaush();
    }

    public AuthResponse loginUser(String email, String password) {
        logger.info("Processing login request for user: {}", email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("user not found"));
                
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            
            String token = jwtService.generateToken(email, user.getRole());
            logger.debug("Login successful for user: {} with role: {}", email, user.getRole());
            return new AuthResponse(token, user.getRole());
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", email, e.getMessage());
            throw e;
        }
    }
}

package com.ai.eachdaycounts.service;

import com.ai.eachdaycounts.dto.AuthResponse;
import com.ai.eachdaycounts.entity.User;
import com.ai.eachdaycounts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse registerUser(String email, String password) {
        if(userRepository.existsByEmail((email))){
            throw new RuntimeException("Email already exists!!");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER"); // Set default role
        userRepository.save(user);
        
        String token = jwtService.generateToken(email, user.getRole());
        return new AuthResponse(token);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getNaushEntry() {
        return userRepository.findNaush();
    }

    public AuthResponse loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
                
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtService.generateToken(email, user.getRole());
        return new AuthResponse(token);
    }
}

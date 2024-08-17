package com.master.user_management.controller;

import com.master.user_management.dto.request.UserLoginDTO;
import com.master.user_management.dto.response.AuthResponse;
import com.master.user_management.entity.User;
import com.master.user_management.security.JwtTokenProvider;
import com.master.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        log.info("Authenticating user with username: {}", userLoginDTO.getUsername());

        try {
            Optional<User> userOptional = userService.authenticateUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String token = jwtTokenProvider.createToken(user.getUsername(), List.of());
                AuthResponse response = new AuthResponse("success", null, token);
                log.info("Authentication successful for user: {}", userLoginDTO.getUsername());
                return ResponseEntity.ok(response);
            } else {
                log.warn("Authentication failed for user: {}", userLoginDTO.getUsername());
                AuthResponse response = new AuthResponse("error", "Invalid username or password.", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("Error occurred during authentication", e);
            AuthResponse response = new AuthResponse("error", "An error occurred during authentication.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

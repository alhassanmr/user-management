package com.master.user_management.service;

import com.master.user_management.dto.UserDTO;
import com.master.user_management.dto.request.UserUpdateDTO;
import com.master.user_management.entity.RoleName;
import com.master.user_management.entity.User;
import com.master.user_management.dto.request.UserRegistrationDTO;
import com.master.user_management.exception.ResourceNotFoundException;
import com.master.user_management.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDTO userRegistrationDTO) {
        log.info("Registering new user with username: {}", userRegistrationDTO.getUsername());

        try {
            // Check if the username or email already exists
            if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
                log.warn("Username {} is already taken", userRegistrationDTO.getUsername());
                throw new IllegalArgumentException("Username already exists");
            }

            if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
                log.warn("Email {} is already registered", userRegistrationDTO.getEmail());
                throw new IllegalArgumentException("Email already exists");
            }

            String hashedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());

            LocalDateTime now = LocalDateTime.now();
            RoleName roleName = userRegistrationDTO.getRoleName();

            User user = new User(userRegistrationDTO.getUsername(), hashedPassword, userRegistrationDTO.getEmail(), now, now, roleName);

            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}", savedUser.getId());

            return savedUser;
        } catch (IllegalArgumentException e) {
            log.error("Validation error during registration for username: {}. Error: {}", userRegistrationDTO.getUsername(), e.getMessage());
            throw e; // Rethrow validation exceptions to be handled by the controller
        } catch (Exception e) {
            log.error("Error occurred while registering user with username: {}", userRegistrationDTO.getUsername(), e);
            throw new RuntimeException("User registration failed", e);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> authenticateUser(String username, String password) {
        log.info("Authenticating user with username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            log.info("Authentication successful for user: {}", username);
            return user;
        }
        log.warn("Authentication failed for user: {}", username);
        return Optional.empty();
    }

    @Override
    public User findUserById(Long id) {
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id " + id);
                });
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        log.info("Updating user with ID: {}", id);
        try {
            User user = findUserById(id);

            user.setUsername(userUpdateDTO.getUsername());
            user.setEmail(userUpdateDTO.getEmail());
            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            log.info("User with ID: {} updated successfully", id);
            return updatedUser;
        } catch (ResourceNotFoundException e) {
            log.error("User update failed: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Database access error occurred while updating user with ID: {}: {}", id, e.getMessage());
            throw new RuntimeException("An error occurred while updating the user. Please try again later.");
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating user with ID: {}: {}", id, e.getMessage());
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("User not found with ID: {}", id);
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
        log.info("User with ID: {} deleted successfully", id);
    }
}


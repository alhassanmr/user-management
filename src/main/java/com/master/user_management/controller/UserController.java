package com.master.user_management.controller;

import com.master.user_management.dto.UserDTO;
import com.master.user_management.dto.request.UserRegistrationDTO;
import com.master.user_management.dto.request.UserUpdateDTO;
import com.master.user_management.dto.response.ApiResponse;
import com.master.user_management.entity.User;
import com.master.user_management.exception.ResourceNotFoundException;
import com.master.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.master.user_management.util.Constants.SUCCESS;
import static com.master.user_management.util.Constants.ERROR;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        log.info("Registering new user with username: {}", userRegistrationDTO.getUsername());

        try {
            User user = userService.registerUser(userRegistrationDTO);
            log.info("User registered successfully with ID: {}", user.getId());

            UserDTO userDTO = new UserDTO(user);
            ApiResponse<UserDTO> response = new ApiResponse<>(SUCCESS, "User registered successfully", userDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error during registration: {}", e.getMessage());
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error occurred during registration: {}", e.getMessage(), e);
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, "User registration failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        try {
            Page<UserDTO> users = userService.findAll(pageable).map(UserDTO::new);
            ApiResponse<Page<UserDTO>> response = new ApiResponse<>(SUCCESS, "Users retrieved successfully", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while fetching users: {}", e.getMessage(), e);
            ApiResponse<Page<UserDTO>> response = new ApiResponse<>(ERROR, "Failed to fetch users", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);

        try {
            User user = userService.findUserById(id);
            ApiResponse<UserDTO> response = new ApiResponse<>(SUCCESS, "User retrieved successfully", new UserDTO(user));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.warn("User with ID: {} not found", id);
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("Error occurred while fetching user with ID: {}", id, e);
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, "Failed to fetch user", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        log.info("Updating user with ID: {}", id);

        try {
            User updatedUser = userService.updateUser(id, userUpdateDTO);
            ApiResponse<UserDTO> response = new ApiResponse<>(SUCCESS, "User updated successfully", new UserDTO(updatedUser));
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.warn("User with ID: {} not found", id);
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error during update: {}", e.getMessage());
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error occurred while updating user with ID: {}", id, e);
            ApiResponse<UserDTO> response = new ApiResponse<>(ERROR, "User update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        log.info("Request received to delete user with ID: {}", id);

        try {
            userService.deleteUserById(id);
            log.info("User with ID: {} successfully deleted.", id);
            ApiResponse<String> response = new ApiResponse<>(SUCCESS, "User deleted successfully", "User with ID: " + id + " has been deleted.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.warn("User with ID: {} not found. Deletion aborted.", id);
            ApiResponse<String> response = new ApiResponse<>(ERROR, "User not found", "User with ID: " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("An error occurred while deleting user with ID: {}", id, e);
            ApiResponse<String> response = new ApiResponse<>(ERROR, "Failed to delete user", "An error occurred while attempting to delete the user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
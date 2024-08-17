package com.master.user_management.controller;

import com.master.user_management.dto.UserDTO;
import com.master.user_management.dto.request.UserRegistrationDTO;
import com.master.user_management.dto.request.UserUpdateDTO;
import com.master.user_management.entity.User;
import com.master.user_management.exception.ResourceNotFoundException;
import com.master.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        log.info("Registering new user with username: {}", userRegistrationDTO.getUsername());
        User user = userService.registerUser(userRegistrationDTO);
        log.info("User registered successfully with ID: {}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        log.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userService.findUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        log.info("Updating user with ID: {}", id);
        User updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("Request received to delete user with ID: {}", id);

        try {
            userService.deleteUserById(id);
            log.info("User with ID: {} successfully deleted.", id);
            return ResponseEntity.ok("User with ID: " + id + " has been deleted.");
        } catch (ResourceNotFoundException e) {
            log.warn("User with ID: {} not found. Deletion aborted.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + id + " not found.");
        } catch (Exception e) {
            log.error("An error occurred while deleting user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while attempting to delete the user.");
        }
    }

}

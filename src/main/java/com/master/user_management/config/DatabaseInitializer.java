package com.master.user_management.config;

import com.master.user_management.entity.User;
import com.master.user_management.repository.UserRepository;
import com.master.user_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.master.user_management.entity.RoleName.ROLE_ADMIN;

@Component
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Optional<User> existingSuperUser = userService.findUserByUsername("admin");
        log.info("Checking if a super user with username 'admin' exists.");
        if (existingSuperUser.isEmpty()) {
            log.info("Super user with username 'admin' does not exist. Creating a new super user.");
            LocalDateTime now = LocalDateTime.now();
            User user = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com", now, now, ROLE_ADMIN);
            userRepository.save(user);
            log.info("Super user 'admin' created successfully");
        } else {
            log.info("Super user with username 'admin' already exists.");
        }

    }
}

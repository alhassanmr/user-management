package com.master.user_management.service;

import com.master.user_management.dto.UserDTO;
import com.master.user_management.dto.request.UserRegistrationDTO;
import com.master.user_management.dto.request.UserUpdateDTO;
import com.master.user_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDTO userRegistrationDTO);
    Optional<User> authenticateUser(String username, String password);
    User findUserById(Long id);
    User updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUserById(Long id);
    List<UserDTO> getAllUsers();
    Page<User> findAll(Pageable pageable);
}


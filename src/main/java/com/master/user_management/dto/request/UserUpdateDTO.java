package com.master.user_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;
}


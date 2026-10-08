package com.sbm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;
    private Boolean isActive;
    private String roleName;
}
